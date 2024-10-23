window.GiohangController = function ($scope, $http) {
  $scope.cart = []; // Initialize an empty cart array
  const userId = 3; // Replace with actual user ID or fetch dynamically
  const cartId = 1;
  $scope.getCartItems = function () {
    // Replace with the actual cart ID if needed
    $http
      .get(`http://localhost:8080/api/giohang/${cartId}`)
      .then(function (response) {
        $scope.cart = response.data; // Assign the fetched data to cart
        $scope.cart.forEach((element) => {
          $http
            .get("http://localhost:8080/api/hinh_anh/" + element.idSanPham)
            .then(async function (response) {
              element.urlAnh = (await response.data[0].urlAnh) || [];
            })
            .catch(function (error) {
              console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
            });
        });
  
        const newCartItem = localStorage.getItem("cartItem");
        if (newCartItem) {
          const item = JSON.parse(newCartItem);
         
          $scope.cart.push(item);
          localStorage.removeItem("cartItem"); // Clear the stored item after adding it
        }
      })
      .catch(function (error) {
        console.error("Error fetching cart items:", error);
      });
  };

  $scope.removeFromCart = function (idSanPhamChiTiet) {
    $http
      .delete(
        `http://localhost:8080/api/giohang/delete?idGioHang=${cartId}&idSanPhamChiTiet=${idSanPhamChiTiet}`
      )
      .then(function (response) {
        alert(response.data.message);
        $scope.getCartItems();
      })
      .catch(function (error) {
        console.error("Lỗi xóa sản phẩm khỏi giỏ hàng:", error);
      });
  };

  $scope.updateCart = function (item) {
    let value = {
      soLuong: item.soLuong,
      donGia: item.giaTien,
      idSanPhamChiTiet: item.idspct,
      thanhTien: item.soLuong * item.giaTien,
    };
    $http({
      method: "PUT",
      url: `http://localhost:8080/api/giohang/update?idGioHang=${cartId}`,
      data: value,
      headers: {
        "Content-Type": "application/json", // Thiết lập header
      },
    })
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        console.error("Lỗi xóa sản phẩm khỏi giỏ hàng:", error);
      });
  };
  $scope.getTotal = function () {
    let total = 0;
    $scope.cart.forEach((item) => {
      total += item.soLuong * item.giaTien;
    });
    return total;
  };
  $scope.getCartItems();
};
