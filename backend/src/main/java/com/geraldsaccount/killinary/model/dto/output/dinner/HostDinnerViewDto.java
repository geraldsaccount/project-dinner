package com.geraldsaccount.killinary.model.dto.output.dinner;

import com.geraldsaccount.killinary.model.dto.output.detail.ConclusionDto;
import com.geraldsaccount.killinary.model.dto.output.detail.HostInfoDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateInfoDto;

public record HostDinnerViewDto(
        PreDinnerInfoDto preDinnerInfo,
        PrivateInfoDto privateInfo,
        ConclusionDto conclusion,
        HostInfoDto hostInfo) implements DinnerView {

}
