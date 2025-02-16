package blaybus.hair_mvp.domain.designer.entity;

import lombok.Getter;

@Getter
public enum MeetingType {
    OFFLINE("대면"), ONLINE("비대면"), BOTH("대면, 비대면");

    private final String displayName;

    MeetingType(String meetingType) {
        this.displayName = meetingType;
    }

}
