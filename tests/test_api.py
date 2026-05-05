"""
家具销售系统 API 自动化测试脚本
用法: python test_api.py
前提: 确保 Spring Boot 后端已启动 (http://localhost:8080)
"""

import requests
import json
import time
import sys

BASE_URL = "http://localhost:8080"
HEADERS = {"Content-Type": "application/json"}

passed = 0
failed = 0
token = None

def test(name, func):
    global passed, failed
    try:
        func()
        passed += 1
        print(f"  [PASS] {name}")
    except AssertionError as e:
        failed += 1
        print(f"  [FAIL] {name}: {e}")
    except requests.exceptions.ConnectionError:
        failed += 1
        print(f"  [FAIL] {name}: 无法连接服务器，请确认后端已启动")
    except Exception as e:
        failed += 1
        print(f"  [FAIL] {name}: {e}")

def get(path, params=None, auth=True):
    h = HEADERS.copy()
    if auth and token:
        h["Authorization"] = f"Bearer {token}"
    return requests.get(f"{BASE_URL}{path}", params=params, headers=h)

def post(path, data=None, auth=True):
    h = HEADERS.copy()
    if auth and token:
        h["Authorization"] = f"Bearer {token}"
    return requests.post(f"{BASE_URL}{path}", json=data, headers=h)

def put(path, data=None, auth=True):
    h = HEADERS.copy()
    if auth and token:
        h["Authorization"] = f"Bearer {token}"
    return requests.put(f"{BASE_URL}{path}", json=data, headers=h)

def delete(path, auth=True):
    h = HEADERS.copy()
    if auth and token:
        h["Authorization"] = f"Bearer {token}"
    return requests.delete(f"{BASE_URL}{path}", headers=h)


# ========================
# 1. 服务器连通性测试
# ========================
def test_server_available():
    print("\n--- 1. 服务器连通性 ---")
    
    def check_health():
        r = requests.get(f"{BASE_URL}/api/products", timeout=5)
        assert r.status_code in [200, 401, 403], f"状态码异常: {r.status_code}"
    
    test("服务器可达", check_health)


# ========================
# 2. 用户认证 API 测试
# ========================
def test_auth():
    global token
    print("\n--- 2. 用户认证 ---")

    def register_new_user():
        data = {
            "username": f"test_{int(time.time())}",
            "password": "Test123456",
            "name": "测试用户",
            "phone": "13800138000",
            "email": f"test{int(time.time())}@example.com"
        }
        r = post("/api/users/register", data, auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"
        resp = r.json()
        assert resp.get("success") == True, f"注册失败: {resp}"

    def login():
        global token
        # 尝试多种登录方式
        attempts = [
            {"username": "admin", "password": "123456"},
            {"username": "admin", "password": "admin"},
            {"username": "user1", "password": "user1"},
            {"username": "user1", "password": "123456"},
            {"username": "test", "password": "123456"},
        ]
        for creds in attempts:
            r = requests.post(f"{BASE_URL}/api/users/login", params=creds, headers=HEADERS)
            if r.status_code == 200:
                resp = r.json()
                if resp.get("success"):
                    data_resp = resp.get("data", {})
                    t = data_resp.get("token") if isinstance(data_resp, dict) else None
                    if t:
                        token = t
                        return
        assert False, f"无法登录，已尝试 {len(attempts)} 组凭据"

    def login_wrong_password():
        data = {"username": "user1", "password": "wrong_password"}
        r = requests.post(f"{BASE_URL}/api/users/login", params=data, headers=HEADERS)
        resp = r.json()
        assert resp.get("success") == False, "错误密码应登录失败"

    def login_empty_username():
        data = {"username": "", "password": "123456"}
        r = requests.post(f"{BASE_URL}/api/users/login", params=data, headers=HEADERS)
        assert r.status_code in [200, 400], f"状态码: {r.status_code}"

    test("用户注册", register_new_user)
    test("用户登录 (正确凭据)", login)
    test("用户登录 (错误密码)", login_wrong_password)
    test("用户登录 (空用户名)", login_empty_username)


# ========================
# 3. 商品 API 测试
# ========================
def test_products():
    print("\n--- 3. 商品管理 ---")

    def get_product_list():
        r = get("/api/products", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"
        resp = r.json()
        assert resp.get("success") == True, f"获取失败: {resp}"
        data = resp.get("data", {})
        if isinstance(data, dict):
            assert "list" in data or "total" in data, f"分页数据格式错误"

    def get_product_list_paginated():
        r = get("/api/products", params={"page": 1, "pageSize": 5}, auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def get_product_detail():
        r = get("/api/products/1", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"
        resp = r.json()
        assert resp.get("success") == True, f"获取失败: {resp}"

    def get_product_not_exist():
        r = get("/api/products/99999", auth=False)
        assert r.status_code in [200, 404], f"状态码: {r.status_code}"

    test("获取商品列表", get_product_list)
    test("分页获取商品", get_product_list_paginated)
    test("获取商品详情", get_product_detail)
    test("获取不存在的商品", get_product_not_exist)


# ========================
# 4. 分类 API 测试
# ========================
def test_categories():
    print("\n--- 4. 分类管理 ---")

    def get_category_list():
        r = get("/api/categories", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("获取分类列表", get_category_list)


# ========================
# 5. 购物车 API 测试
# ========================
def test_cart():
    print("\n--- 5. 购物车 ---")

    def add_to_cart():
        data = {"productId": 1, "quantity": 1}
        r = post("/api/cart", data)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def get_cart():
        r = get("/api/cart")
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def add_cart_no_auth():
        h = HEADERS.copy()
        r = requests.post(f"{BASE_URL}/api/cart", json={"productId": 1, "quantity": 1}, headers=h)
        assert r.status_code in [401, 403], f"未认证应拒绝, 实际: {r.status_code}"

    test("加入购物车", add_to_cart)
    test("获取购物车", get_cart)
    test("未认证访问购物车被拒绝", add_cart_no_auth)


# ========================
# 6. 订单 API 测试
# ========================
def test_orders():
    print("\n--- 6. 订单管理 ---")

    def get_order_list():
        r = get("/api/orders")
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def get_order_detail():
        r = get("/api/orders/202603130001")
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("获取订单列表", get_order_list)
    test("获取订单详情", get_order_detail)


# ========================
# 7. 地址 API 测试
# ========================
def test_addresses():
    print("\n--- 7. 地址管理 ---")

    def add_address():
        data = {
            "name": "测试用户",
            "phone": "13800138000",
            "province": "广东省",
            "city": "深圳市",
            "district": "南山区",
            "detailAddress": "科技园路1号"
        }
        r = post("/api/addresses", data)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def get_addresses():
        r = get("/api/addresses")
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("添加地址", add_address)
    test("获取地址列表", get_addresses)


# ========================
# 8. 评价 API 测试
# ========================
def test_reviews():
    print("\n--- 8. 评价管理 ---")

    def get_reviews():
        r = get("/api/reviews/product/14", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def get_latest_reviews():
        r = get("/api/review/latest", auth=True)
        assert r.status_code in [200, 404], f"状态码: {r.status_code}, 响应: {r.text}"

    test("获取商品评价", get_reviews)
    test("获取最新评价", get_latest_reviews)


# ========================
# 9. 客服消息 API 测试
# ========================
def test_customer_service():
    print("\n--- 9. 客服消息 ---")

    def get_all_messages():
        r = get("/api/customer-service/messages/all", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def send_message():
        params = {
            "senderId": 5,
            "senderName": "测试用户",
            "recipientId": 0,
            "message": "自动化测试消息"
        }
        r = requests.post(f"{BASE_URL}/api/customer-service/send", params=params, headers=HEADERS)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def get_user_messages():
        r = get("/api/customer-service/messages", params={"userId": 5})
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def mark_read():
        r = requests.put(f"{BASE_URL}/api/customer-service/messages/read-all", params={"userId": 5}, headers=HEADERS)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    test("获取所有消息", get_all_messages)
    test("发送消息", send_message)
    test("获取用户消息", get_user_messages)
    test("标记已读", mark_read)


# ========================
# 10. 轮播图 API 测试
# ========================
def test_carousels():
    print("\n--- 10. 轮播图 ---")

    def get_carousels():
        r = get("/api/carousels", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("获取轮播图列表", get_carousels)


# ========================
# 11. 活动 API 测试
# ========================
def test_activities():
    print("\n--- 11. 活动管理 ---")

    def get_activities():
        r = get("/api/activities", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("获取活动列表", get_activities)


# ========================
# 12. 统计 API 测试
# ========================
def test_statistics():
    print("\n--- 12. 数据统计 ---")

    def get_dashboard():
        r = get("/api/statistics/dashboard", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    def get_sales():
        r = get("/api/statistics/sales", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}"

    test("获取仪表盘数据", get_dashboard)
    test("获取销售统计", get_sales)


# ========================
# 13. 统一响应格式验证
# ========================
def test_response_format():
    print("\n--- 13. 响应格式验证 ---")

    def check_result_structure():
        endpoints = [
            ("GET", "/api/products", None, False),
            ("GET", "/api/categories", None, False),
            ("GET", "/api/activities", None, False),
            ("GET", "/api/carousels", None, False),
        ]
        for method, path, data, auth in endpoints:
            r = requests.request(method, f"{BASE_URL}{path}", json=data, headers=HEADERS)
            resp = r.json()
            assert "success" in resp, f"{path} 缺少 success 字段"
            assert "message" in resp, f"{path} 缺少 message 字段"
            assert "data" in resp, f"{path} 缺少 data 字段"

    test("所有接口返回统一 Result 格式", check_result_structure)


# ========================
# 14. 收藏 API 测试
# ========================
def test_favorites():
    print("\n--- 14. 收藏管理 ---")

    def get_favorites():
        r = get("/api/favorites/list")
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def add_favorite():
        h = HEADERS.copy()
        if token:
            h["Authorization"] = f"Bearer {token}"
        r = requests.post(f"{BASE_URL}/api/favorites/add", params={"productId": 1}, headers=h)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def check_favorite():
        h = HEADERS.copy()
        if token:
            h["Authorization"] = f"Bearer {token}"
        r = requests.get(f"{BASE_URL}/api/favorites/check", params={"productId": 1}, headers=h)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    test("获取收藏列表", get_favorites)
    test("添加收藏", add_favorite)
    test("检查收藏状态", check_favorite)


# ========================
# 15. 权限验证测试
# ========================
def test_auth_required():
    print("\n--- 15. 权限验证 ---")

    def protected_endpoints():
        endpoints = [
            ("GET", "/api/cart"),
            ("GET", "/api/orders"),
            ("GET", "/api/addresses"),
            ("GET", "/api/favorites/list"),
        ]
        for method, path in endpoints:
            h = HEADERS.copy()
            r = requests.request(method, f"{BASE_URL}{path}", headers=h)
            assert r.status_code in [401, 403], \
                f"{path} 应拒绝未认证请求, 实际: {r.status_code}"

    test("受保护接口需要认证", protected_endpoints)


# ========================
# 16. 推荐系统 API 测试
# ========================
def test_recommendations():
    print("\n--- 17. 推荐系统 ---")

    def get_user_recommend():
        r = get("/api/recommendations/user/5", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def get_product_recommend():
        r = get("/api/recommendations/product/1", auth=False)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def record_view():
        r = requests.post(f"{BASE_URL}/api/recommendations/view?userId=5&productId=1", headers=HEADERS)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    def record_purchase():
        r = requests.post(f"{BASE_URL}/api/recommendations/purchase?userId=5&productId=2", headers=HEADERS)
        assert r.status_code == 200, f"状态码: {r.status_code}, 响应: {r.text}"

    test("获取用户推荐", get_user_recommend)
    test("获取相关商品推荐", get_product_recommend)
    test("记录浏览行为", record_view)
    test("记录购买行为(之前未调用)", record_purchase)


# ========================
# 运行所有测试
# ========================
if __name__ == "__main__":
    print("=" * 50)
    print("  家具销售系统 API 自动化测试")
    print("=" * 50)
    print(f"  目标地址: {BASE_URL}")
    print(f"  开始时间: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 50)

    try:
        test_server_available()
        
        if failed > 0:
            print("\n[警告] 服务器连接失败，跳过后续测试。请先启动后端服务。")
            print(f"\n结果: {passed} 通过, {failed} 失败")
            sys.exit(1)

        test_auth()
        test_products()
        test_categories()
        test_carousels()
        test_activities()
        test_statistics()
        test_cart()
        test_orders()
        test_addresses()
        test_reviews()
        test_customer_service()
        test_favorites()
        test_recommendations()
        test_response_format()
        test_auth_required()

    except KeyboardInterrupt:
        print("\n\n测试被用户中断")

    finally:
        total = passed + failed
        print("\n" + "=" * 50)
        print(f"  测试完成: {passed}/{total} 通过")
        if failed > 0:
            print(f"  失败: {failed}")
        print(f"  结束时间: {time.strftime('%Y-%m-%d %H:%M:%S')}")
        print("=" * 50)
