package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.XacThuc;
import com.example.duantn.repository.NguoiDungRepository;
import com.example.duantn.repository.XacThucRepository;
import com.example.duantn.TokenUser.JwtTokenUtil;
import com.example.duantn.TokenUser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class XacThucService {

    @Autowired
    private XacThucRepository xacThucRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository; // Nếu cần làm việc với thực thể người dùng
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Tạo một mục xác thực mới
    public XacThuc createXacThuc(XacThuc xacThuc) {
        xacThuc.setNgayTao(new Date());  // Đặt ngày tạo
        xacThuc.setNgayCapNhat(new Date());  // Đặt ngày cập nhật
        return xacThucRepository.save(xacThuc); // Lưu bản ghi xác thực mới
    }

    // Lấy tất cả các bản ghi xác thực
    public List<XacThuc> getAllXacThuc() {
        return xacThucRepository.findAll(); // Trả về tất cả các bản ghi từ cơ sở dữ liệu
    }

    // Lấy một bản ghi xác thực theo ID
    public XacThuc getXacThucById(Integer idXacThuc) {
        return xacThucRepository.findById(idXacThuc)
                .orElseThrow(() -> new RuntimeException("Bản ghi xác thực không tồn tại với ID: " + idXacThuc));
    }

    // Cập nhật một bản ghi xác thực
    public XacThuc updateXacThuc(Integer idXacThuc, XacThuc xacThucDetails) {
        XacThuc existingXacThuc = xacThucRepository.findById(idXacThuc)
                .orElseThrow(() -> new RuntimeException("Bản ghi xác thực không tồn tại với ID: " + idXacThuc));

        // Cập nhật các trường
        existingXacThuc.setMaXacThuc(xacThucDetails.getMaXacThuc());
        existingXacThuc.setMoTa(xacThucDetails.getMoTa());
        existingXacThuc.setNguoiDung(xacThucDetails.getNguoiDung()); // Đặt người dùng liên quan nếu cần
        existingXacThuc.setNgayCapNhat(new Date()); // Đặt ngày cập nhật

        return xacThucRepository.save(existingXacThuc); // Lưu bản ghi xác thực đã cập nhật
    }

    // Xóa một bản ghi xác thực theo ID
    public void deleteXacThuc(Integer idXacThuc) {
        XacThuc xacThuc = xacThucRepository.findById(idXacThuc)
                .orElseThrow(() -> new RuntimeException("Bản ghi xác thực không tồn tại với ID: " + idXacThuc));

        xacThucRepository.delete(xacThuc); // Xóa bản ghi xác thực
    }

    public Token xacThuc(String maXacThuc) {
        XacThuc xacThuc = xacThucRepository.findByMaXacThuc(maXacThuc);
        if (xacThuc == null) {
            throw new RuntimeException("Mã xác thực không hợp lệ.");
        }

        NguoiDung nguoiDung = nguoiDungRepository.findById(xacThuc.getNguoiDung().getIdNguoiDung())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        nguoiDung.setTrangThai(true); // Cập nhật trạng thái người dùng thành đã xác thực
        nguoiDungRepository.save(nguoiDung); // Lưu lại thay đổi

        xacThucRepository.delete(xacThuc); // Xóa mã xác thực đã dùng

        // Tạo access token và refresh token
        UserDetails userDetails = ourUserDetailsService.loadUserByUsername(nguoiDung.getEmail());
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return new Token(accessToken, refreshToken);
    }

}
