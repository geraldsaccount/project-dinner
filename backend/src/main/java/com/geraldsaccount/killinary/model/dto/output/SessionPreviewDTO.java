package com.geraldsaccount.killinary.model.dto.output;

import java.util.Set;

import lombok.Builder;

@Builder
public record SessionPreviewDTO(
        UserDTO host,
        StoryPreviewDTO story,
        Set<RoleAssignmentDTO> assignedRoles) {

}
