window.HoaDonController = function ($scope, $http) {
    $scope.hoaDonList = [];
    $scope.filteredHoaDonList = [];
    $scope.searchQuery = "";
    $scope.searchName = "";
    $scope.searchPhone = "";
    $scope.searchTrangThai = "";
    
    
    $scope.updateTrangThaiHoaDon = function(idHoaDon) {
        const idTrangThaiMoi = $scope.newStatusId; 
        const url = `http://localhost:8080/api/hoa-don/cap-nhat-trang-thai/${idHoaDon}?idTrangThaiMoi=${idTrangThaiMoi}`;
    
        $http.put(url)
            .then(function(response) {
                alert("Cập nhật trạng thái hóa đơn thành công!");
                $scope.getHoaDonDetails();
                $scope.closeAddCustomerModal();
            })
            
    };

    $scope.openInvoiceStatusModal = function (hoaDon) {
        $scope.selectedInvoice = hoaDon;
        $scope.isAddCustomerModalOpen = true;
        console.log('Thông tin hóa đơn:', hoaDon);
    };
    $scope.closeAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = false;
        $scope.selectedInvoice = null;
    };
    $scope.getHoaDonDetails = function () {
        $http.get('http://localhost:8080/api/hoa-don/get-hoa-don')
            .then(function (response) {
                $scope.hoaDonList = response.data;
                $scope.filteredHoaDonList = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching data:', error);
            });
    };
    $scope.filterHoaDon = function () {
        console.log('Trạng thái đang tìm kiếm:', $scope.searchTrangThai);
        $scope.filteredHoaDonList = $scope.hoaDonList.filter(function (hoaDon) {
            const matchesQuery = hoaDon.maHoaDon.toLowerCase().includes($scope.searchQuery.toLowerCase());
            const matchesName = hoaDon.tenNguoiDung.toLowerCase().includes($scope.searchName.toLowerCase());
            const matchesPhone = hoaDon.sdtNguoiDung && hoaDon.sdtNguoiDung.includes($scope.searchPhone);
            const matchesStatus = !$scope.searchTrangThai || hoaDon.tenTrangThai === $scope.searchTrangThai;
            const matchesType = !$scope.searchLoaiHoaDon || hoaDon.loai === $scope.searchLoaiHoaDon;
            console.log(`Hóa đơn: ${hoaDon.maHoaDon}, Trạng thái: ${hoaDon.tenTrangThai}, Khớp với trạng thái: ${matchesStatus}`);
            return matchesQuery && matchesName && matchesPhone && matchesStatus && matchesType;
        });

        if (!$scope.searchQuery && !$scope.searchName && !$scope.searchPhone && !$scope.searchTrangThai && !$scope.searchLoaiHoaDon) {
            $scope.filteredHoaDonList = $scope.hoaDonList;
        }
        console.log('Danh sách hóa đơn sau khi lọc:', $scope.filteredHoaDonList);
    };
    $scope.filterNgay = function () {
        const startDate = new Date($scope.searchStartDate);
        const endDate = new Date($scope.searchEndDate);
        if (endDate < startDate) {
            alert('Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.');
            return;
        }
        $scope.filteredHoaDonList = $scope.hoaDonList.filter(function (hoaDon) {
            const hoaDonDate = new Date(hoaDon.ngayTao);
            const withinDateRange = (!startDate || hoaDonDate >= startDate) &&
                (!endDate || hoaDonDate <= endDate);

            return withinDateRange;
        });

        if (!$scope.searchStartDate && !$scope.searchEndDate) {
            $scope.filteredHoaDonList = $scope.hoaDonList;
        }
        $scope.searchStartDate = null;
        $scope.searchEndDate = null;
    }
    $scope.confirmExportExcel = function() {
        $scope.isExportExcelModalOpen = true;
    };

    $scope.closeExportModal = function() {
        $scope.isExportExcelModalOpen = false;
    };
    $scope.exportHoaDonToExcel = function () {
        const url = 'http://localhost:8080/api/hoa-don/export';
        $http.get(url, { responseType: 'blob' })
            .then(function (response) {
                const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = 'hoa_don.xlsx';
                link.click();
                $scope.closeExportModal();
            })
            .catch(function (error) {
                console.error('Error exporting data:', error);
            });
    };

    $scope.getHoaDonDetails();
};
