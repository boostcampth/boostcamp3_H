package teamh.boostcamp.myapplication.utils;

import android.content.Context;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

public class KakaoLinkHelperImpl implements KakaoLinkHelper {

    private static final String IMAGE_URL = "https://user-images.githubusercontent.com/24218456/52896087-1ed55200-3206-11e9-8132-a7e2b5254615.png";
    private static final String USER_ID = "user_id";
    private static final String PRODUCT_ID = "product_id";
    private Context context;

    public KakaoLinkHelperImpl(Context context) {
        this.context = context;
    }

    @Override
    public void sendDiary(DiaryEntity diaryEntity) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년 MM월 dd일");

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("오늘 하루의 목소리",
                        IMAGE_URL,
                        LinkObject.newBuilder().setWebUrl(diaryEntity.getRecordFilePath())
                                .setMobileWebUrl(diaryEntity.getRecordFilePath()).build())
                        .setDescrption(String.format(context.getString(R.string.kakao_description),
                                simpleDateFormat.format(diaryEntity.getRecordDate()),
                                diaryEntity.getSelectedEmotion().getEmoji(),
                                diaryEntity.getAnalyzedEmotion().getEmoji()))
                        .build())
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<>();
        serverCallbackArgs.put(USER_ID, "${current_user_id}");
        serverCallbackArgs.put(PRODUCT_ID, "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }
}
