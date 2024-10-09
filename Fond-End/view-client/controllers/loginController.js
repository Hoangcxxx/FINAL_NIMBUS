// window.LoginController = function($scope, $http, $window) {
//     $scope.login = function() {
//         var userCredentials = {
//             email: $scope.email,
//             matKhau: $scope.password
//         };

//         $http.post('http://localhost:8080/api/auth/login', userCredentials)
//             .then(function(response) {
//                 // Kiểm tra xem token và tên người dùng có được trả về hay không
//                 if (response.data.token && response.data.tenNguoiDung) {
//                     // Lưu JWT token và tên người dùng vào localStorage
//                     localStorage.setItem('jwtToken', response.data.token);
//                     localStorage.setItem('tenNguoiDung', response.data.tenNguoiDung);
//                     // Hiển thị thông báo thành công
//                     $scope.successMessage = 'Đăng nhập thành công!';
//                     $scope.errorMessage = ''; // Xóa thông báo lỗi nếu có
//                     // Chuyển hướng đến trang chủ sau 1 giây để người dùng nhìn thấy thông báo
//                     setTimeout(function() {
//                         $window.location.href = '/index.html';
//                     }, 1000);
//                 } else {
//                     // Nếu không có token hoặc tên người dùng, báo lỗi
//                     $scope.errorMessage = 'Phản hồi không hợp lệ từ máy chủ.';
//                     $scope.successMessage = ''; // Xóa thông báo thành công nếu có
//                 }
//             })
//             .catch(function(error) {
//                 // Kiểm tra nếu server trả về thông báo lỗi cụ thể
//                 if (error.data && error.data.message) {
//                     $scope.errorMessage = error.data.message;
//                 } else {
//                     $scope.errorMessage = 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
//                 }
//                 $scope.successMessage = ''; // Xóa thông báo thành công nếu có
//             });
//     };
// };
