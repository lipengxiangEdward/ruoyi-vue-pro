package cn.iocoder.yudao.module.promotion.convert.coupon;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template.CouponTemplateUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 优惠劵模板 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CouponTemplateConvert {

    CouponTemplateConvert INSTANCE = Mappers.getMapper(CouponTemplateConvert.class);

    CouponTemplateDO convert(CouponTemplateCreateReqVO bean);

    CouponTemplateDO convert(CouponTemplateUpdateReqVO bean);

    CouponTemplateRespVO convert(CouponTemplateDO bean);

    PageResult<CouponTemplateRespVO> convertPage(PageResult<CouponTemplateDO> page);

    CouponTemplatePageReqVO convert(AppCouponTemplatePageReqVO pageReqVO, List<Integer> canTakeTypes, Integer productScope, Long productScopeValue);

    PageResult<AppCouponTemplateRespVO> convertAppPage(PageResult<CouponTemplateDO> pageResult);

    default PageResult<AppCouponTemplateRespVO> convertAppPage(PageResult<CouponTemplateDO> pageResult, Map<Long, Integer> couponTakeCountMap) {
        PageResult<AppCouponTemplateRespVO> result = convertAppPage(pageResult);
        if (MapUtil.isEmpty(couponTakeCountMap)) {
            return result;
        }

        for (AppCouponTemplateRespVO vo : result.getList()) {
            // 每人领取数量无限制
            if (vo.getTakeLimitCount() == -1) {
                vo.setTakeStatus(false);
                continue;
            }
            // 检查已领取数量是否超过限领数量
            vo.setTakeStatus(MapUtil.getInt(couponTakeCountMap, vo.getId(), 0) >= vo.getTakeLimitCount());
        }
        return result;
    }
}
