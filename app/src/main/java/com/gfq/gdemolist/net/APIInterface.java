package com.gfq.gdemolist.net;



import com.gfq.gdemolist.bean.BixinBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.gfq.gdemolist.net.APIService.BASE_URL;

/**
 * retrofit 使用
 * ·@Path：所有在网址中的参数（URL的问号前面），如：http://102.10.10.132/api/Accounts/{accountId}
 * <p>
 * ·Query：URL问号后面的参数，如：http://102.10.10.132/api/Comments?access_token={access_token}
 * <p>
 * ·QueryMap：相当于多个@Query
 * <p>
 * ·Field：用于POST请求，提交单个数据
 * <p>
 * ·Body：相当于多个@Field，以对象的形式提交
 */
public interface APIInterface {
    /**
     * 登录
     * @param map
     * {
     *   "userName": "string",
     *   "userPwd": "string"
     * }
     * @return
     */
  /*  @POST(BASE_URL+"/Api/FTISWebAPI/FunLogin")
    Observable<User> login(@Body Map<String, String> map);
*/

    @POST(BASE_URL+"/recommend/v2/timeline/recommend")
    Observable<BixinBean> bixin(@Body Map<String, Object> map);


}

