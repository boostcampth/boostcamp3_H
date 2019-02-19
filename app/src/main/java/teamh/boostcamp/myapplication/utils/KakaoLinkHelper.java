package teamh.boostcamp.myapplication.utils;

import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.ShareDiary;

public interface KakaoLinkHelper {
    void sendDiary(DiaryEntity diaryEntity);
}
