package com.furniture.service.impl;

import com.furniture.entity.Product;
import com.furniture.entity.UserBehavior;
import com.furniture.mapper.UserBehaviorMapper;
import com.furniture.service.RecommendationService;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    
    // 使用内存存储替代Redis
    private Map<String, Set<Integer>> memoryStorage = new HashMap<>();
    private Map<Integer, UserProfile> userProfiles = new HashMap<>();
    private Map<Integer, ProductProfile> productProfiles = new HashMap<>();
    
    private static final String USER_VIEW_KEY = "user:view:";
    private static final String USER_PURCHASE_KEY = "user:purchase:";
    private static final String PRODUCT_SIMILARITY_KEY = "product:similarity:";
    
    // 用户偏好配置
    private static final double VIEW_WEIGHT = 0.1;
    private static final double PURCHASE_WEIGHT = 0.5;
    private static final double RATING_WEIGHT = 0.3;
    private static final double RECENCY_WEIGHT = 0.1;
    
    @Override
    public List<Product> recommendByUserId(Integer userId, int limit) {
        // 获取用户历史行为
        Set<Integer> viewedProductIds = memoryStorage.getOrDefault(USER_VIEW_KEY + userId, new HashSet<>());
        Set<Integer> purchasedProductIds = memoryStorage.getOrDefault(USER_PURCHASE_KEY + userId, new HashSet<>());
        
        // 合并历史记录
        Set<Integer> historyProductIds = new HashSet<>();
        historyProductIds.addAll(viewedProductIds);
        historyProductIds.addAll(purchasedProductIds);
        
        // 构建用户画像
        UserProfile userProfile = buildUserProfile(userId, viewedProductIds, purchasedProductIds);
        
        // 基于多种策略推荐
        List<RecommendedProduct> recommendedProducts = new ArrayList<>();
        
        // 1. 基于协同过滤的推荐
        List<RecommendedProduct> collaborativeRecommendations = getCollaborativeRecommendations(userId, historyProductIds, limit);
        recommendedProducts.addAll(collaborativeRecommendations);
        
        // 2. 基于内容的推荐
        List<RecommendedProduct> contentRecommendations = getContentRecommendations(userProfile, historyProductIds, limit);
        recommendedProducts.addAll(contentRecommendations);
        
        // 3. 热门产品推荐
        if (recommendedProducts.size() < limit) {
            List<RecommendedProduct> popularRecommendations = getPopularRecommendations(historyProductIds, limit - recommendedProducts.size());
            recommendedProducts.addAll(popularRecommendations);
        }
        
        // 去重并排序
        Map<Integer, RecommendedProduct> uniqueProducts = new HashMap<>();
        for (RecommendedProduct rp : recommendedProducts) {
            if (!historyProductIds.contains(rp.getProduct().getId())) {
                if (!uniqueProducts.containsKey(rp.getProduct().getId()) || 
                    rp.getScore() > uniqueProducts.get(rp.getProduct().getId()).getScore()) {
                    uniqueProducts.put(rp.getProduct().getId(), rp);
                }
            }
        }
        
        // 按得分排序并限制数量
        return uniqueProducts.values().stream()
            .sorted(Comparator.comparing(RecommendedProduct::getScore).reversed())
            .limit(limit)
            .map(RecommendedProduct::getProduct)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> recommendByProductId(Integer productId, int limit) {
        Product targetProduct = productService.findById(productId);
        if (targetProduct == null) {
            return Collections.emptyList();
        }
        
        // 构建产品画像
        ProductProfile targetProfile = buildProductProfile(targetProduct);
        
        // 计算与其他产品的相似度
        List<RecommendedProduct> similarProducts = new ArrayList<>();
        List<Product> allProducts = productService.findAll();
        
        for (Product product : allProducts) {
            if (!product.getId().equals(productId)) {
                ProductProfile profile = buildProductProfile(product);
                double similarity = calculateProductSimilarity(targetProfile, profile);
                if (similarity > 0.1) { // 只考虑相似度大于0.1的产品
                    similarProducts.add(new RecommendedProduct(product, similarity));
                }
            }
        }
        
        // 按相似度排序并限制数量
        return similarProducts.stream()
            .sorted(Comparator.comparing(RecommendedProduct::getScore).reversed())
            .limit(limit)
            .map(RecommendedProduct::getProduct)
            .collect(Collectors.toList());
    }
    
    @Override
    public void recordUserView(Integer userId, Integer productId) {
        // 记录用户浏览行为到数据库
        saveUserBehaviorToDatabase(userId, productId, "view");
        
        // 记录用户浏览行为到内存
        String viewKey = USER_VIEW_KEY + userId;
        memoryStorage.computeIfAbsent(viewKey, k -> new HashSet<>()).add(productId);
        
        // 更新用户画像
        updateUserProfile(userId, productId, VIEW_WEIGHT);
        
        // 更新产品相似度
        updateProductSimilarity(userId, productId);
    }
    
    @Override
    public void recordUserPurchase(Integer userId, Integer productId) {
        // 记录用户购买行为到数据库
        saveUserBehaviorToDatabase(userId, productId, "buy");
        
        // 记录用户购买行为到内存
        String purchaseKey = USER_PURCHASE_KEY + userId;
        memoryStorage.computeIfAbsent(purchaseKey, k -> new HashSet<>()).add(productId);
        
        // 更新用户画像（购买权重更高）
        updateUserProfile(userId, productId, PURCHASE_WEIGHT);
        
        // 更新产品相似度
        updateProductSimilarity(userId, productId);
    }
    
    // 构建用户画像
    private UserProfile buildUserProfile(Integer userId, Set<Integer> viewedProductIds, Set<Integer> purchasedProductIds) {
        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile());
        
        // 从数据库加载用户行为
        loadUserBehaviorsFromDatabase(userId);
        
        // 处理浏览历史
        for (Integer productId : viewedProductIds) {
            Product product = productService.findById(productId);
            if (product != null) {
                profile.addPreference(product.getCategoryId(), VIEW_WEIGHT);
                profile.addPricePreference(product.getPrice().doubleValue(), VIEW_WEIGHT);
            }
        }
        
        // 处理购买历史（权重更高）
        for (Integer productId : purchasedProductIds) {
            Product product = productService.findById(productId);
            if (product != null) {
                profile.addPreference(product.getCategoryId(), PURCHASE_WEIGHT);
                profile.addPricePreference(product.getPrice().doubleValue(), PURCHASE_WEIGHT);
            }
        }
        
        return profile;
    }
    
    // 构建产品画像
    private ProductProfile buildProductProfile(Product product) {
        ProductProfile profile = productProfiles.computeIfAbsent(product.getId(), k -> new ProductProfile());
        profile.setCategoryId(product.getCategoryId());
        profile.setPrice(product.getPrice());
        profile.setRating(product.getRating() != null ? product.getRating().doubleValue() : 0.0);
        return profile;
    }
    
    // 更新用户画像
    private void updateUserProfile(Integer userId, Integer productId, double weight) {
        Product product = productService.findById(productId);
        if (product != null) {
            UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile());
            profile.addPreference(product.getCategoryId(), weight);
            profile.addPricePreference(product.getPrice().doubleValue(), weight);
        }
    }
    
    // 更新产品相似度
    private void updateProductSimilarity(Integer userId, Integer productId) {
        Set<Integer> viewedProductIds = memoryStorage.get(USER_VIEW_KEY + userId);
        if (viewedProductIds != null) {
            for (Integer viewedId : viewedProductIds) {
                if (!viewedId.equals(productId)) {
                    memoryStorage.computeIfAbsent(PRODUCT_SIMILARITY_KEY + productId, k -> new HashSet<>()).add(viewedId);
                    memoryStorage.computeIfAbsent(PRODUCT_SIMILARITY_KEY + viewedId, k -> new HashSet<>()).add(productId);
                }
            }
        }
    }
    
    // 基于协同过滤的推荐
    private List<RecommendedProduct> getCollaborativeRecommendations(Integer userId, Set<Integer> historyProductIds, int limit) {
        List<RecommendedProduct> recommendations = new ArrayList<>();
        
        // 查找与当前用户有相似行为的其他用户
        Map<Integer, Double> userSimilarities = new HashMap<>();
        
        for (String key : memoryStorage.keySet()) {
            if (key.startsWith(USER_PURCHASE_KEY)) {
                int otherUserId = Integer.parseInt(key.substring(USER_PURCHASE_KEY.length()));
                if (otherUserId != userId) {
                    Set<Integer> otherUserPurchases = memoryStorage.get(key);
                    double similarity = calculateUserSimilarity(historyProductIds, otherUserPurchases);
                    if (similarity > 0.1) {
                        userSimilarities.put(otherUserId, similarity);
                    }
                }
            }
        }
        
        // 基于相似用户的购买行为推荐
        Map<Integer, Double> productScores = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : userSimilarities.entrySet()) {
            int otherUserId = entry.getKey();
            double similarity = entry.getValue();
            
            Set<Integer> otherUserPurchases = memoryStorage.get(USER_PURCHASE_KEY + otherUserId);
            if (otherUserPurchases != null) {
                for (Integer productId : otherUserPurchases) {
                    if (!historyProductIds.contains(productId)) {
                        productScores.put(productId, productScores.getOrDefault(productId, 0.0) + similarity);
                    }
                }
            }
        }
        
        // 转换为推荐产品列表
        for (Map.Entry<Integer, Double> entry : productScores.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null) {
                recommendations.add(new RecommendedProduct(product, entry.getValue()));
            }
        }
        
        return recommendations;
    }
    
    // 基于内容的推荐
    private List<RecommendedProduct> getContentRecommendations(UserProfile userProfile, Set<Integer> historyProductIds, int limit) {
        List<RecommendedProduct> recommendations = new ArrayList<>();
        List<Product> allProducts = productService.findAll();
        
        for (Product product : allProducts) {
            if (!historyProductIds.contains(product.getId())) {
                ProductProfile productProfile = buildProductProfile(product);
                double score = calculateUserProductScore(userProfile, productProfile);
                if (score > 0.1) {
                    recommendations.add(new RecommendedProduct(product, score));
                }
            }
        }
        
        return recommendations;
    }
    
    // 热门产品推荐
    private List<RecommendedProduct> getPopularRecommendations(Set<Integer> historyProductIds, int limit) {
        List<RecommendedProduct> recommendations = new ArrayList<>();
        List<Product> allProducts = productService.findAll();
        
        // 基于评分和假设的销量排序（实际应该从数据库获取销量）
        allProducts.sort((p1, p2) -> {
            double score1 = (p1.getRating() != null ? p1.getRating().doubleValue() : 0) * 0.7 + Math.random() * 0.3;
            double score2 = (p2.getRating() != null ? p2.getRating().doubleValue() : 0) * 0.7 + Math.random() * 0.3;
            return Double.compare(score2, score1);
        });
        
        int count = 0;
        for (Product product : allProducts) {
            if (!historyProductIds.contains(product.getId()) && count < limit) {
                recommendations.add(new RecommendedProduct(product, 0.5)); // 基础得分
                count++;
            }
        }
        
        return recommendations;
    }
    
    // 计算用户相似度
    private double calculateUserSimilarity(Set<Integer> user1Products, Set<Integer> user2Products) {
        if (user1Products.isEmpty() || user2Products.isEmpty()) {
            return 0.0;
        }
        
        Set<Integer> intersection = new HashSet<>(user1Products);
        intersection.retainAll(user2Products);
        
        Set<Integer> union = new HashSet<>(user1Products);
        union.addAll(user2Products);
        
        return (double) intersection.size() / union.size();
    }
    
    // 计算产品相似度
    private double calculateProductSimilarity(ProductProfile p1, ProductProfile p2) {
        double similarity = 0.0;
        
        // 分类相似度（相同分类为1，不同为0）
        if (p1.getCategoryId().equals(p2.getCategoryId())) {
            similarity += 0.5;
        }
        
        // 价格相似度（基于价格差异的倒数）
        double priceDiff = Math.abs(p1.getPrice() - p2.getPrice());
        double priceSimilarity = 1.0 / (1.0 + priceDiff / 1000); // 价格差异越小，相似度越高
        similarity += priceSimilarity * 0.3;
        
        // 评分相似度
        double ratingDiff = Math.abs(p1.getRating() - p2.getRating());
        double ratingSimilarity = 1.0 - ratingDiff / 5.0; // 评分差异越小，相似度越高
        similarity += ratingSimilarity * 0.2;
        
        return similarity;
    }
    
    // 计算用户对产品的得分
    private double calculateUserProductScore(UserProfile userProfile, ProductProfile productProfile) {
        double score = 0.0;
        
        // 分类偏好得分
        Double categoryPreference = userProfile.getCategoryPreferences().get(productProfile.getCategoryId());
        if (categoryPreference != null) {
            score += categoryPreference * 0.6;
        }
        
        // 价格偏好得分
        double pricePreference = userProfile.getPricePreference(productProfile.getPrice());
        score += pricePreference * 0.3;
        
        // 评分得分
        score += productProfile.getRating() / 5.0 * 0.1;
        
        return score;
    }
    
    // 用户画像类
    private static class UserProfile {
        private Map<Integer, Double> categoryPreferences = new HashMap<>();
        private List<PriceRange> pricePreferences = new ArrayList<>();
        
        public void addPreference(Integer categoryId, double weight) {
            categoryPreferences.put(categoryId, categoryPreferences.getOrDefault(categoryId, 0.0) + weight);
        }
        
        public void addPricePreference(Double price, double weight) {
            // 简单处理：记录价格范围偏好
            int priceRange = (int) (price / 1000) * 1000;
            boolean found = false;
            for (PriceRange range : pricePreferences) {
                if (range.getStart() == priceRange) {
                    range.setWeight(range.getWeight() + weight);
                    found = true;
                    break;
                }
            }
            if (!found) {
                pricePreferences.add(new PriceRange(priceRange, priceRange + 999, weight));
            }
        }
        
        public double getPricePreference(double price) {
            int priceRange = (int) (price / 1000) * 1000;
            for (PriceRange range : pricePreferences) {
                if (range.getStart() == priceRange) {
                    return range.getWeight();
                }
            }
            return 0.1; // 默认偏好
        }
        
        public Map<Integer, Double> getCategoryPreferences() {
            return categoryPreferences;
        }
    }
    
    // 价格范围类
    private static class PriceRange {
        private int start;
        private int end;
        private double weight;
        
        public PriceRange(int start, int end, double weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
        
        public int getStart() {
            return start;
        }
        
        public double getWeight() {
            return weight;
        }
        
        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
    
    // 产品画像类
    private static class ProductProfile {
        private Integer categoryId;
        private Double price;
        private double rating;
        
        public Integer getCategoryId() {
            return categoryId;
        }
        
        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }
        
        public Double getPrice() {
            return price;
        }
        
        public void setPrice(Double price) {
            this.price = price;
        }
        
        public void setPrice(java.math.BigDecimal price) {
            this.price = price != null ? price.doubleValue() : null;
        }
        
        public double getRating() {
            return rating;
        }
        
        public void setRating(double rating) {
            this.rating = rating;
        }
    }
    
    // 推荐产品类
    private static class RecommendedProduct {
        private Product product;
        private double score;
        
        public RecommendedProduct(Product product, double score) {
            this.product = product;
            this.score = score;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public double getScore() {
            return score;
        }
    }
    
    // 保存用户行为到数据库
    private void saveUserBehaviorToDatabase(Integer userId, Integer productId, String behaviorType) {
        try {
            UserBehavior userBehavior = new UserBehavior();
            userBehavior.setUserId(userId);
            userBehavior.setProductId(productId);
            userBehavior.setBehaviorType(behaviorType);
            userBehaviorMapper.insert(userBehavior);
        } catch (Exception e) {
            // 记录错误但不影响正常流程
            e.printStackTrace();
        }
    }
    
    // 从数据库加载用户行为
    private void loadUserBehaviorsFromDatabase(Integer userId) {
        try {
            List<UserBehavior> behaviors = userBehaviorMapper.getByUserId(userId);
            if (behaviors != null && !behaviors.isEmpty()) {
                for (UserBehavior behavior : behaviors) {
                    if ("view".equals(behavior.getBehaviorType())) {
                        String viewKey = USER_VIEW_KEY + userId;
                        memoryStorage.computeIfAbsent(viewKey, k -> new HashSet<>()).add(behavior.getProductId());
                    } else if ("buy".equals(behavior.getBehaviorType())) {
                        String purchaseKey = USER_PURCHASE_KEY + userId;
                        memoryStorage.computeIfAbsent(purchaseKey, k -> new HashSet<>()).add(behavior.getProductId());
                    }
                }
            }
        } catch (Exception e) {
            // 记录错误但不影响正常流程
            e.printStackTrace();
        }
    }
}