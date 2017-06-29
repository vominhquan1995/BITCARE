package asia.health.bitcare.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.network.APIConstant;

/**
 * Created by HP on 29-Dec-16.
 */

public class User {
    public static final String MALE = "M";
    public static final String FEMALE = "F";
    /**
     * Only one User instance throw application
     */
    private static User instance;
    private final String TAG = getClass().getSimpleName();
    private String userNm;
    private String gender;
    private String userBtDate;
    private String mPhoneNo1;
    private String mPhoneNo2;
    private String mPhoneNo3;
    private String userId;
    private String userPw;
    private double height;
    private double weight;
    private long userNum;

    private User() {

    }

    public static User get() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserBtDate() {
        return userBtDate;
    }

    public void setUserBtDate(String userBtDate) {
        this.userBtDate = userBtDate;
    }

    public String getMPhoneNo1() {
        return mPhoneNo1;
    }

    public void setMPhoneNo1(String mPhoneNo1) {
        this.mPhoneNo1 = mPhoneNo1;
    }

    public String getMPhoneNo2() {
        return mPhoneNo2;
    }

    public void setMPhoneNo2(String mPhoneNo2) {
        this.mPhoneNo2 = mPhoneNo2;
    }

    public String getMPhoneNo3() {
        return mPhoneNo3;
    }

    public void setMPhoneNo3(String mPhoneNo3) {
        this.mPhoneNo3 = mPhoneNo3;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getUserNum() {
        return userNum;
    }

    public void setUserNum(long userNum) {
        this.userNum = userNum;
    }

    public void init(JSONObject response) {
        try {
            JSONArray userInfo = response.getJSONArray(APIConstant.USERINFO);
            if (userInfo.length() > 0) {
                JSONObject data = (JSONObject) userInfo.get(0);
                setUserId(data.getString(APIConstant.USERID));
                setUserNum(data.getLong(APIConstant.USERNUM));
                setUserNm(data.getString(APIConstant.USERNM));
                setGender(data.getString(APIConstant.GENDER));
                setHeight(data.getDouble(APIConstant.HEIGHT));
                setWeight(data.getDouble(APIConstant.WEIGHT));
                setMPhoneNo1(data.getString(APIConstant.MPHONENO1));
                setMPhoneNo2(data.getString(APIConstant.MPHONENO2));
                setMPhoneNo3(data.getString(APIConstant.MPHONENO3));
                setUserBtDate(data.getString(APIConstant.USERBTDATE));
                Log.d(TAG, "Init user success !");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
