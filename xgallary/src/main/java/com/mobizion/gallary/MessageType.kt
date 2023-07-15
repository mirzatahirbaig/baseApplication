package com.mobizion.gallary

enum class MessageType(val value: String) {
    TEXT("text"), IMAGE("image"), PHOTOS("photos"), VIDEO("video"),AUDIO("audio"),
    LINK("link"), LOCATION("location"), PDF("pdf"), GIF("GIF"),
    CONTACT("contact"), AUDIO_CALL("audioCall"), VIDEO_CALL("videoCall"),
    PROFILE_PIC_UPDATE("profilePicUpdate"), PROFILE_DELETED("profileDeleted"),
    PROFILE_NAME_UPDATE("profileNameUpdate"),
    PROFILE_PHONE_UPDATE("profilePhoneUpdate")
}