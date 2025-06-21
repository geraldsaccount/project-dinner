package com.geraldsaccount.killinary.model.dto.output.dinner;

import com.geraldsaccount.killinary.model.dto.output.detail.ConclusionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateInfoDto;

public record GuestDinnerViewDto(
        PreDinnerInfoDto preDinnerInfo,
        PrivateInfoDto privateInfo,
        ConclusionDto conclusion) implements DinnerView {

}
