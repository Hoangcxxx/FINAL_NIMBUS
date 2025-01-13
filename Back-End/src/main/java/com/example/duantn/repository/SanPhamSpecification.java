package com.example.duantn.repository;

import com.example.duantn.entity.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;

public class SanPhamSpecification {

    // Phương thức lọc theo giá sản phẩm
    public static Specification<SanPham> withPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("giaBan"), minPrice, maxPrice);
            }
            return null;
        };
    }

    // Phương thức lọc theo danh mục sản phẩm
    public static Specification<SanPham> withDanhMuc(Integer danhMucId) {
        return (root, query, criteriaBuilder) -> {
            if (danhMucId != null) {
                return criteriaBuilder.equal(root.get("danhMuc").get("idDanhMuc"), danhMucId);
            }
            return null;
        };
    }

    // Phương thức lọc theo chất liệu của sản phẩm chi tiết
    public static Specification<SanPham> withChatLieu(Integer chatLieuId) {
        return (root, query, criteriaBuilder) -> {
            if (chatLieuId != null) {
                // Join với SanPhamChiTiet và ChatLieuChiTiet để kiểm tra chất liệu
                Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTiet");
                Join<SanPhamChiTiet, ChatLieuChiTiet> chatLieuJoin = sanPhamChiTietJoin.join("chatLieuChiTiet");
                return criteriaBuilder.equal(chatLieuJoin.get("chatLieu").get("id"), chatLieuId);
            }
            return null;
        };
    }

    // Phương thức lọc theo màu sắc của sản phẩm chi tiết
    public static Specification<SanPham> withMauSac(Integer mauSacId) {
        return (root, query, criteriaBuilder) -> {
            if (mauSacId != null) {
                // Join với SanPhamChiTiet và MauSacChiTiet để kiểm tra màu sắc
                Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTiet");
                Join<SanPhamChiTiet, MauSacChiTiet> mauSacJoin = sanPhamChiTietJoin.join("mauSacChiTiet");
                return criteriaBuilder.equal(mauSacJoin.get("mauSac").get("id"), mauSacId);
            }
            return null;
        };
    }

    // Phương thức lọc theo kích thước của sản phẩm chi tiết
    public static Specification<SanPham> withKichThuoc(Integer kichThuocId) {
        return (root, query, criteriaBuilder) -> {
            if (kichThuocId != null) {
                // Join với SanPhamChiTiet và KichThuocChiTiet để kiểm tra kích thước
                Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTiet");
                Join<SanPhamChiTiet, KichThuocChiTiet> kichThuocJoin = sanPhamChiTietJoin.join("kichThuocChiTiet");
                return criteriaBuilder.equal(kichThuocJoin.get("kichThuoc").get("id"), kichThuocId);
            }
            return null;
        };
    }

    // Phương thức join với SanPhamChiTiet (bắt buộc cho việc lọc chi tiết sản phẩm)
    public static Specification<SanPham> withSanPhamChiTiet() {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra có join với SanPhamChiTiet không
            root.join("sanPhamChiTiet");
            return null;
        };
    }
}
