package asia.health.bitcare.network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.model.User;
import asia.health.bitcare.model.Weight;

/**
 * Created by HP on 30-Dec-16.
 */

public class API {
    private static String TAG = "API";
    private static OnAPIListener listener;

    /**
     * Check connection
     */
    public static void checkConnection(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.CHECK_CONNECTION_URL)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            if (response.getInt(APIConstant.RESULTCODE) == 0) {
                                listener.onSuccess(response, "");
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), "");
                            }
                        } catch (JsonParseException | JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError(error.getMessage(), "");
                    }
                });
    }

    /**
     * User Login
     *
     * @param userId
     * @param userPass
     * @param onAPIListener
     */
    public static void login(final String userId, final String userPass, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.LOGIN_URL)
                .addBodyParameter(APIConstant.USERID, userId)
                .addBodyParameter(APIConstant.USERPW, userPass)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                if (response.getString(APIConstant.SERVICEMSG).equals(APIConstant.LOGIN_FAIL_MESSAGE)) {
                                    listener.onError(APIConstant.LOGIN_FAIL_MESSAGE, serviceMsg);
                                }else if(response.getString(APIConstant.SERVICEMSG).equals(APIConstant.WITHDRAW_ACCOUNT_MESSAGE))
                                {
                                    listener.onError(APIConstant.WITHDRAW_ACCOUNT_MESSAGE, serviceMsg);
                                }
                                else {
                                    listener.onSuccess(response, serviceMsg);
                                }
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException | JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError(error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Blood Pressure List
     *
     * @param type
     * @param onAPIListener
     */
    public static void getBloodPressureList(String type, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.BP_LIST_URL)
                .addBodyParameter(APIConstant.USERNUM, User.get().getUserNum() + "")
                .addBodyParameter(APIConstant.BPMSTYPE, type)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                Log.d(TAG, "onResponse: success");
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                Log.d(TAG, "onResponse: error");
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Blood Sugar list
     *
     * @param type
     * @param onAPIListener
     */
    public static void getBloodSugarList(String type, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.BS_LIST_URL)
                .addBodyParameter(APIConstant.USERNUM, User.get().getUserNum() + "")
                .addBodyParameter(APIConstant.BSMSTYPE, type)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Weight list
     *
     * @param onAPIListener
     */
    public static void getWeightList(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.WEIGHT_LIST_URL)
                .addBodyParameter(APIConstant.USERNUM, User.get().getUserNum() + "")
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get userID
     *
     * @param userName
     * @param birth
     * @param mPhoneNo1
     * @param mPhoneNo2
     * @param mPhoneNo3
     * @param onAPIListener
     */
    public static void getId(String userName, String birth,
                             String mPhoneNo1, String mPhoneNo2, String mPhoneNo3, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ID_SEARCH)
                .addBodyParameter(APIConstant.USERNM, userName)
                .addBodyParameter(APIConstant.USERBTDATE, birth)
                .addBodyParameter(APIConstant.MPHONENO1, mPhoneNo1)
                .addBodyParameter(APIConstant.MPHONENO2, mPhoneNo2)
                .addBodyParameter(APIConstant.MPHONENO3, mPhoneNo3)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("Service code : " + error.getMessage(), "");
                        Log.d(TAG, error.getMessage());
                    }
                });
    }

    /**
     * Get user password
     *
     * @param userID
     * @param birth
     * @param mPhoneNo1
     * @param mPhoneNo2
     * @param mPhoneNo3
     * @param onAPIListener
     */
    public static void getPassword(String userID, String birth,
                                   String mPhoneNo1, String mPhoneNo2, String mPhoneNo3, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.PW_SEARCH)
                .addBodyParameter(APIConstant.USERID, userID)
                .addBodyParameter(APIConstant.USERBTDATE, birth)
                .addBodyParameter(APIConstant.MPHONENO1, mPhoneNo1)
                .addBodyParameter(APIConstant.MPHONENO2, mPhoneNo2)
                .addBodyParameter(APIConstant.MPHONENO3, mPhoneNo3)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("Service code : " + error.getMessage(), "");
                        Log.d(TAG, error.getMessage());
                    }
                });
    }

    /**
     * Modify password
     *
     * @param userNum
     * @param newPass
     * @param onAPIListener
     */
    public static void modifyPassword(String userNum, String newPass, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.PW_MODIFY)
                .addBodyParameter(APIConstant.USERNUM, userNum)
                .addBodyParameter(APIConstant.USERPW, newPass)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                if (response.getInt(APIConstant.RESULTCODE) == 0)
                                    listener.onSuccess(response, serviceMsg);
                                else {
                                    listener.onError("Service code : " + response.getInt(APIConstant.RESULTCODE), serviceMsg);
                                }
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "Index of result code : " + error.getMessage().indexOf(APIConstant.RESULTCODE));

                        //TODO remove it
                        //Search char
                        if (error.getMessage().charAt(error.getMessage().indexOf(APIConstant.RESULTCODE) +
                                APIConstant.RESULTCODE.length() + 2) == 48) {
                            try {
                                listener.onSuccess(null,"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, error.getMessage());
                            listener.onError("Service code : " + error.getMessage(),"");
                        }
                    }
                });
    }

    /**
     * Add BloodPressure data
     *
     * @param bloodPressure
     * @param onAPIListener
     */
    public static void addBPData(BloodPressure bloodPressure, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ADD_BP)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.BPMSTYPE, bloodPressure.getBpmStyle())
                .addBodyParameter(APIConstant.BPSYS, String.valueOf(bloodPressure.getBpSys()))
                .addBodyParameter(APIConstant.BPMIN, String.valueOf(bloodPressure.getBpMin()))
                .addBodyParameter(APIConstant.BPPULSE, String.valueOf(bloodPressure.getBpPulse()))
                .addBodyParameter(APIConstant.BPWEIGHT, String.valueOf(bloodPressure.getBpWeight()))
                .addBodyParameter(APIConstant.BPMEDICINEYN, bloodPressure.getBpMedicineYN())
                .addBodyParameter(APIConstant.BPEXERCISEYN, bloodPressure.getBpExersiseYN())
                .addBodyParameter(APIConstant.BPMSDATE, bloodPressure.getBpmsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //TODO remove it
                        //Search char
                        if (error.getMessage().charAt(error.getMessage().indexOf(APIConstant.RESULTCODE) +
                                APIConstant.RESULTCODE.length() + 2) == 48) {
                            try {
                                listener.onSuccess(null,"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, error.getMessage());
                            listener.onError("Service code : " + error.getMessage(),"");
                        }
                    }
                });
    }

    /**
     * Add blood sugar
     *
     * @param bloodSugar
     * @param onAPIListener
     */
    public static void addBSData(BloodSugar bloodSugar, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ADD_BS)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.BSMSTYPE, bloodSugar.getBsmStyle())
                .addBodyParameter(APIConstant.BSTYPE, String.valueOf(bloodSugar.getBsType()))
                .addBodyParameter(APIConstant.BSVAL, String.valueOf(bloodSugar.getBsVal()))
                .addBodyParameter(APIConstant.BSWEIGHT, String.valueOf(bloodSugar.getBsWeight()))
                .addBodyParameter(APIConstant.BSMEDICINEYN, bloodSugar.getBsMedicineYN())
                .addBodyParameter(APIConstant.BSEXERCISEYN, bloodSugar.getBsExersiseYN())
                .addBodyParameter(APIConstant.BSMSDATE, bloodSugar.getBsmsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "Index of result code : " + error.getMessage().indexOf(APIConstant.RESULTCODE));

                        //TODO remove it
                        //Search char
                        if (error.getMessage().charAt(error.getMessage().indexOf(APIConstant.RESULTCODE) +
                                APIConstant.RESULTCODE.length() + 2) == 48) {
                            try {
                                listener.onSuccess(null,"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, error.getMessage());
                            listener.onError("Service code : " + error.getMessage(),"");
                        }
                    }
                });
    }

    /**
     * Add new Weight data
     *
     * @param weight
     * @param onAPIListener
     */
    public static void addWeightData(Weight weight, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ADD_WEIGHT)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.WEIGHT, String.valueOf(weight.getWeight()))
                .addBodyParameter(APIConstant.MSDATE, weight.getMsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * modify User
     *
     * @param userNm
     * @param gender
     * @param height
     * @param weight
     * @param userDate
     * @param mPhoneNo1
     * @param mPhoneNo2
     * @param mPhoneNo3
     * @param onAPIListener
     */
    public static void modifyUser(final String userNm, final String gender, final String height, final String weight,
                                  final String userDate, final String mPhoneNo1, final String mPhoneNo2, final String mPhoneNo3,
                                  OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.USER_MODIFY)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.USERNM, userNm)
                .addBodyParameter(APIConstant.GENDER, gender)
                .addBodyParameter(APIConstant.HEIGHT, height)
                .addBodyParameter(APIConstant.WEIGHT, weight)
                .addBodyParameter(APIConstant.USERBTDATE, userDate)
                .addBodyParameter(APIConstant.MPHONENO1, mPhoneNo1)
                .addBodyParameter(APIConstant.MPHONENO2, mPhoneNo2)
                .addBodyParameter(APIConstant.MPHONENO3, mPhoneNo3)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                updateCurrentUser();
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    private void updateCurrentUser() {
                        User.get().setUserNm(userNm);
                        User.get().setGender(gender);
                        User.get().setWeight(Double.parseDouble(weight));
                        User.get().setHeight(Double.parseDouble(height));
                        User.get().setUserBtDate(userDate);
                        User.get().setMPhoneNo1(mPhoneNo1);
                        User.get().setMPhoneNo2(mPhoneNo2);
                        User.get().setMPhoneNo3(mPhoneNo3);
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Registration Init
     *
     * @param userNm
     * @param gender
     * @param userDate
     * @param mPhoneNo1
     * @param mPhoneNo2
     * @param mPhoneNo3
     * @param onAPIListener
     */
    public static void registrationInit(final String userNm, final String gender, final String userDate, final String mPhoneNo1,
                                        final String mPhoneNo2, final String mPhoneNo3, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.REGISTER_INIT_URL)
                .addBodyParameter(APIConstant.USERNM, userNm)
                .addBodyParameter(APIConstant.GENDER, gender)
                .addBodyParameter(APIConstant.USERBTDATE, userDate)
                .addBodyParameter(APIConstant.MPHONENO1, mPhoneNo1)
                .addBodyParameter(APIConstant.MPHONENO2, mPhoneNo2)
                .addBodyParameter(APIConstant.MPHONENO3, mPhoneNo3)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Registration User
     *
     * @param userID
     * @param userPW
     * @param userNm
     * @param gender
     * @param height
     * @param weight
     * @param userDate
     * @param mPhoneNo1
     * @param mPhoneNo2
     * @param mPhoneNo3
     * @param onAPIListener
     */
    public static void registration(final String userID, final String userPW, final String userNm, final String gender,
                                    final String height, final String weight, final String userDate, final String mPhoneNo1,
                                    final String mPhoneNo2, final String mPhoneNo3, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.REGISTER_URL)
                .addBodyParameter(APIConstant.USERID, userID)
                .addBodyParameter(APIConstant.USERPW, userPW)
                .addBodyParameter(APIConstant.USERNM, userNm)
                .addBodyParameter(APIConstant.GENDER, gender)
                .addBodyParameter(APIConstant.HEIGHT, height)
                .addBodyParameter(APIConstant.WEIGHT, weight)
                .addBodyParameter(APIConstant.USERBTDATE, userDate)
                .addBodyParameter(APIConstant.MPHONENO1, mPhoneNo1)
                .addBodyParameter(APIConstant.MPHONENO2, mPhoneNo2)
                .addBodyParameter(APIConstant.MPHONENO3, mPhoneNo3)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * User Info
     *
     * @param onAPIListener
     */
    public static void userInfo(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.USER_INFO)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Check ID
     *
     * @param onAPIListener
     */
    public static void idCheck(final String userId, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ID_CHECK)
                .addBodyParameter(APIConstant.LOW_USERID, userId)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            Log.d(TAG, "Count: " + String.valueOf(response.getInt(APIConstant.COUNT)));
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                if (response.getInt(APIConstant.COUNT) == 0) {
                                    listener.onSuccess(response, serviceMsg);
                                } else {
                                    listener.onError("", serviceMsg);
                                }
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Value Health Main
     *
     * @param onAPIListener
     */
    public static void healthMain(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.HEALTH_MAIN)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Last MS Info
     *
     * @param onAPIListener
     */
    public static void lastMsInfo(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.LAST_MS_INFO)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get List Blood Pressuare With Condition Date
     *
     * @param startDate
     * @param endDate
     * @param bpmsType
     * @param onAPIListener
     */
    public static void getBloodPressureListDate(final String startDate, final String endDate, final String bpmsType, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.BP_LIST_DATE)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.STARTDATE, startDate)
                .addBodyParameter(APIConstant.ENDDATE, endDate)
                .addBodyParameter(APIConstant.BPMSTYPE, bpmsType)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Modify Data Blood Pressure
     *
     * @param bloodPressure
     * @param onAPIListener
     */
    public static void modifyBPData(BloodPressure bloodPressure, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.MODIFY_BP_DATA)
                .addBodyParameter(APIConstant.BPSEQ, String.valueOf(bloodPressure.getBpSEQ()))
                .addBodyParameter(APIConstant.BPMSTYPE, bloodPressure.getBpmStyle())
                .addBodyParameter(APIConstant.BPMIN, String.valueOf(bloodPressure.getBpMin()))
                .addBodyParameter(APIConstant.BPPULSE, String.valueOf(bloodPressure.getBpPulse()))
                .addBodyParameter(APIConstant.BPWEIGHT, String.valueOf(bloodPressure.getBpWeight()))
                .addBodyParameter(APIConstant.BPMEDICINEYN, bloodPressure.getBpMedicineYN())
                .addBodyParameter(APIConstant.BPEXERCISEYN, bloodPressure.getBpExersiseYN())
                .addBodyParameter(APIConstant.BPMSDATE, bloodPressure.getBpmsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get List Blood Sugar With Condition Date
     *
     * @param startDate
     * @param endDate
     * @param bpmsType
     * @param onAPIListener
     */
    public static void getBloodSugarListDate(final String startDate, final String endDate, final String bpmsType, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.BS_LIST_DATE)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.STARTDATE, startDate)
                .addBodyParameter(APIConstant.ENDDATE, endDate)
                .addBodyParameter(APIConstant.BPMSTYPE, bpmsType)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Modify Data Blood Sugar
     *
     * @param bloodSugar
     * @param onAPIListener
     */
    public static void modifyBSData(BloodSugar bloodSugar, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.MODIFY_BS_DATA)
                .addBodyParameter(APIConstant.BSSEQ, String.valueOf(bloodSugar.getBsSEQ()))
                .addBodyParameter(APIConstant.BSMSTYPE, bloodSugar.getBsmStyle())
                .addBodyParameter(APIConstant.BSVAL, String.valueOf(bloodSugar.getBsVal()))
                .addBodyParameter(APIConstant.BSWEIGHT, String.valueOf(bloodSugar.getBsWeight()))
                .addBodyParameter(APIConstant.BSMEDICINEYN, String.valueOf(bloodSugar.getBsMedicineYN()))
                .addBodyParameter(APIConstant.BSEXERCISEYN, bloodSugar.getBsExersiseYN())
                .addBodyParameter(APIConstant.BSMSDATE, bloodSugar.getBsmsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Modify Data Weight
     *
     * @param weight
     * @param onAPIListener
     */
    public static void modifyWeight(Weight weight, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.MODIFY_WEIGHT_DATA)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.WEIGHT, String.valueOf(weight.getWeight()))
                .addBodyParameter(APIConstant.WEIGHTSEQ, String.valueOf(weight.getWeightSEQ()))
                .addBodyParameter(APIConstant.BSMSDATE, weight.getMsDate())
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Add Fit Weight
     *
     * @param fitWeight
     * @param onAPIListener
     */
    public static void addFitWeight(final double fitWeight, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ADD_FIT_WEIGHT)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.FITWEIGHT, String.valueOf(fitWeight))
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError(e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError(e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        listener.onError("", "");
                    }
                });
    }

    /**
     * Update Version App
     *
     * @param currentVersion
     * @param onAPIListener
     */
    public static void appUpdate(final String currentVersion, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.APP_UPDATE)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.CURRENTVERSION, currentVersion)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Add Health Quest
     *
     * @param smoking
     * @param life
     * @param meat
     * @param fruit
     * @param sleep
     * @param drinking
     * @param bloodp
     * @param diabetes
     * @param exercise
     * @param cold
     * @param personality
     * @param tension
     * @param onAPIListener
     */
    public static void addHealthQuest(final String smoking, final String life, final String meat,
                                      final String fruit, final String sleep, final String drinking,
                                      final String bloodp, final String diabetes,
                                      final String exercise, final String cold, final String personality,
                                      final String tension, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.ADD_HEALTH_QUEST)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.SMOKING, smoking)
                .addBodyParameter(APIConstant.LIFE, life)
                .addBodyParameter(APIConstant.MEAT, meat)
                .addBodyParameter(APIConstant.FRUIT, fruit)
                .addBodyParameter(APIConstant.SLEEP, sleep)
                .addBodyParameter(APIConstant.DRINKING, drinking)
                .addBodyParameter(APIConstant.BLOODP, bloodp)
                .addBodyParameter(APIConstant.DIABETES, diabetes)
                .addBodyParameter(APIConstant.EXERCISE, exercise)
                .addBodyParameter(APIConstant.COLD, cold)
                .addBodyParameter(APIConstant.PERSONALITY, personality)
                .addBodyParameter(APIConstant.TENSION, tension)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Get Info Health Quest
     *
     * @param onAPIListener
     */
    public static void healthQuestInfo(OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.HEALTH_QUEST_INFO)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                JSONArray healthQuestList = response.getJSONArray(APIConstant.HEALTH_QUEST);
                                String data = healthQuestList.get(0).toString();
                                if (!data.equals("null")) {
                                    listener.onSuccess(response, serviceMsg);
                                } else {
                                    listener.onError(null, serviceMsg);
                                }
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }

    /**
     * Modify Health Quest
     *
     * @param healthQSEQ
     * @param smoking
     * @param life
     * @param meat
     * @param fruit
     * @param sleep
     * @param drinking
     * @param bloodp
     * @param diabetes
     * @param exercise
     * @param cold
     * @param personality
     * @param tension
     * @param onAPIListener
     */
    public static void modifyHealthQuest(final String healthQSEQ, final String smoking, final String life, final String meat,
                                         final String fruit, final String sleep, final String drinking,
                                         final String bloodp, final String diabetes,
                                         final String exercise, final String cold, final String personality,
                                         final String tension, OnAPIListener onAPIListener) {
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.MODIFY_HEALTH_QUEST)
                .addBodyParameter(APIConstant.USERNUM, String.valueOf(User.get().getUserNum()))
                .addBodyParameter(APIConstant.HEALTHQSEQ, healthQSEQ)
                .addBodyParameter(APIConstant.SMOKING, smoking)
                .addBodyParameter(APIConstant.LIFE, life)
                .addBodyParameter(APIConstant.MEAT, meat)
                .addBodyParameter(APIConstant.FRUIT, fruit)
                .addBodyParameter(APIConstant.SLEEP, sleep)
                .addBodyParameter(APIConstant.DRINKING, drinking)
                .addBodyParameter(APIConstant.BLOODP, bloodp)
                .addBodyParameter(APIConstant.DIABETES, diabetes)
                .addBodyParameter(APIConstant.EXERCISE, exercise)
                .addBodyParameter(APIConstant.COLD, cold)
                .addBodyParameter(APIConstant.PERSONALITY, personality)
                .addBodyParameter(APIConstant.TENSION, tension)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                                Log.d(TAG, "Service code : " + response.getInt(APIConstant.SERVICECODE));
                            }
                        } catch (JsonParseException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, error.getMessage());
                        listener.onError("Service code : " + error.getMessage(), "");
                    }
                });
    }
    public static  void checkMemberWithdraw(final  String userNum,OnAPIListener onAPIListener){
        listener = onAPIListener;
        AndroidNetworking.post(APIConstant.CHECK_MEMBER_WITHDRAW)
                .addBodyParameter(APIConstant.USERNUM, userNum)
                .setOkHttpClient(NetworkUtil.createDefaultOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            String serviceMsg = response.getString(APIConstant.SERVICEMSG);
                            if (response.getInt(APIConstant.SERVICECODE) == APIConstant.RESULT_SUCCESSFUL) {
                                listener.onSuccess(response, serviceMsg);
                            } else {
                                listener.onError("Service code : " + response.getInt(APIConstant.SERVICECODE), serviceMsg);
                            }
                        } catch (JsonParseException | JSONException e) {
                            Log.d(TAG, e.getMessage());
                            listener.onError("Service code : " + e.getMessage(), "");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        listener.onError(error.getMessage(), "");
                    }
                });
    }

    public interface OnAPIListener {
        void onSuccess(final JSONObject response, String serviceMsg) throws JSONException;

        void onError(String errorMessage, String serviceMsg);
    }
}
