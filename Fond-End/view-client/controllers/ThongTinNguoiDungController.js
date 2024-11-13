window.ThongTinNguoiDungController = function ($scope, $http) {
    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');

    if (userInfo) {
        // Nếu có thông tin người dùng, parse và gán vào scope để hiển thị
        $scope.infoUser = JSON.parse(userInfo);
        $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
        $scope.emailNguoiDung = $scope.infoUser.email;
        $scope.diaChiNguoiDung = $scope.infoUser.diaChi;
        $scope.sdtNguoiDung = $scope.infoUser.sdt;
    } else {
        // Nếu không có thông tin người dùng trong localStorage
        $scope.infoUser = null;
    }
};
