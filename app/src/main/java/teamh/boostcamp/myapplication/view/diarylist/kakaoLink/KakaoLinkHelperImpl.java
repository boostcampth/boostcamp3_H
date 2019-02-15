package teamh.boostcamp.myapplication.view.diarylist.kakaoLink;

import android.content.Context;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.Diary;

public class KakaoLinkHelperImpl implements KakaoLinkHelper {

    Context context;

    public KakaoLinkHelperImpl(Context context) {
        this.context = context;
    }

    @Override
    public void sendDiary(Diary diary) {

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("오늘 하루의 목소리",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(String.format(context.getString(R.string.kakao_description), diary.getSelectedEmotion().getEmoji()))
                        .build())
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

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
