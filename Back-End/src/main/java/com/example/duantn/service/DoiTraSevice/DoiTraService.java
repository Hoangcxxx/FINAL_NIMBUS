package com.example.duantn.service.DoiTraSevice;

import com.example.duantn.dto.DoiTraDTO;
import com.example.duantn.entity.DoiTra;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoiTraService {
    List<DoiTra> createDoiTra(List<DoiTraDTO> doiTraDTOList);
    DoiTra createDoiTra(DoiTraDTO doiTraDTO);    // Tạo yêu cầu đổi trả mới
    List<DoiTra> getDoiTraByHoaDonId(Integer idHoaDon); // Lấy danh sách đổi trả theo hóa đơn

    List<Object[]> getAllSanPhamDoiTraByIdHoaDon(Integer idHoaDon);

    @Transactional
    boolean capNhatTrangThaiHoanTra(Integer idHoaDon, Integer idLoaiTrangThai, Integer idNhanVien );
}
