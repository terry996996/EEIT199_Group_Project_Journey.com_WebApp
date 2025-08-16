package journey.controller.coupons;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.coupons.CouponInstancesBean;
import journey.dto.coupons.CalculateDiscountRequestDTO;
import journey.dto.coupons.CouponDTO;
import journey.service.coupons.CouponService;

@RestController
@RequestMapping("/coupons")
// @CrossOrigin(origins = "http://localhost:8080")
public class CouponController {

    private final CouponService couponService;

    // 設建構子讓couponService自動DI
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/my-coupons")
    public List<CouponDTO> getMyCoupons() {

        return couponService.getCouponsByUserId(1);
    }

    @GetMapping("/usable-coupons")
    public List<CouponInstancesBean> getUsableCoupons() {
        return couponService.getUsableCouponsForUser(1);
    }

    @PostMapping("/calculate-discount")
    public int calculateDiscount(@RequestBody CalculateDiscountRequestDTO request) {
        return couponService.calculateDiscount(request.getCouponInstanceId(), request.getOrderSubtotal());
    }
}
