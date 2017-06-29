package asia.health.bitcare.network;

/**
 * Created by HP on 26-Dec-16.
 */

public interface APIConstant {
    String HOST_NAME = "180.131.18.198";

    //Check connection
    String CHECK_CONNECTION_URL = "http://" + HOST_NAME + "/SmartCare/Connection";

    //Register init
    String REGISTER_INIT_URL = "http://" + HOST_NAME + "/SmartCare/RegistrationInit";

    //Login
    String LOGIN_URL = "http://" + HOST_NAME + "/SmartCare/Login";

    //Register
    String REGISTER_URL = "http://" + HOST_NAME + "/SmartCare/Registration";

    //Get BP list
    String BP_LIST_URL = "http://" + HOST_NAME + "/SmartCare/BPList";

    //Get BS list
    String BS_LIST_URL = "http://" + HOST_NAME + "/SmartCare/BSList";

    //Get Weight list
    String WEIGHT_LIST_URL = "http://" + HOST_NAME + "/SmartCare/WeightList";

    //Forgot ID
    String ID_SEARCH = "http://" + HOST_NAME + "/SmartCare/IDSearch";

    //Forgot password
    String PW_SEARCH = "http://" + HOST_NAME + "/SmartCare/PWSearch";

    //Modify password
    String PW_MODIFY = "http://" + HOST_NAME + "/SmartCare/PWModify";

    //Add BloodPressure data
    String ADD_BP = "http://" + HOST_NAME + "/SmartCare/AddBPData";

    //Add BloodPressure data
    String ADD_BS = "http://" + HOST_NAME + "/SmartCare/AddBSData";

    //Add Weight data
    String ADD_WEIGHT = "http://" + HOST_NAME + "/SmartCare/AddWeight";

    //User modify
    String USER_MODIFY = "http://" + HOST_NAME + "/SmartCare/UserModify";

    //Health Main
    String HEALTH_MAIN = "http://" + HOST_NAME + "/SmartHealthMain";

    //User Info
    String USER_INFO = "http://" + HOST_NAME + "/SmartCare/UserInfo";

    //Id Check
    String ID_CHECK = "http://" + HOST_NAME + "/SmartCare/IDCheck";

    //Last MS Info
    String LAST_MS_INFO = "http://" + HOST_NAME + "/SmartCare/LastMSInfo";

    //Get BP List With Date
    String BP_LIST_DATE = "http://" + HOST_NAME + "/SmartCare/BPList2";

    //Modify BP DATA
    String MODIFY_BP_DATA = "http://" + HOST_NAME + "/SmartCare/ModifyBPData";

    //GET BS List With Date
    String BS_LIST_DATE = "http://" + HOST_NAME + "/SmartCare/BSList2";

    //Modify BS DATA
    String MODIFY_BS_DATA = "http://" + HOST_NAME + "/SmartCare/ModifyBSData";

    //Modify Weight DATA
    String MODIFY_WEIGHT_DATA = "http://" + HOST_NAME + "/SmartCare/ModifyWeight";

    //Add Fit Weight
    String ADD_FIT_WEIGHT = "http://" + HOST_NAME + "/SmartCare/AddFitWeight";

    //App Update
    String APP_UPDATE = "http://" + HOST_NAME + "/SmartCare/AppUpdate";

    //Add Health Quest
    String ADD_HEALTH_QUEST = "http://" + HOST_NAME + "/SmartCare/AddHealthQuest";

    //Health Quest Info
    String HEALTH_QUEST_INFO = "http://" + HOST_NAME + "/SmartCare/HealthQuestInfo";

    //Modify Health Quest
    String MODIFY_HEALTH_QUEST = "http://" + HOST_NAME + "/SmartCare/ModifyHealthQuest";

    //Check Member Withdraw
    String CHECK_MEMBER_WITHDRAW = "http://" + HOST_NAME + "/SmartCare/Check_Member_Withdraw";

    /**
     * Service params
     */
    String SERVICECODE = "serviceCode";
    String SERVICEMSG = "serviceMsg";
    String RESULTCODE = "resultCode";
    String CURRENTVERSION = "1";
    String COUNT = "count";
    int RESULT_SUCCESSFUL = 100;
    int RESULT_SERVER_ERROR = 101;
    int RESULT_DB_ERROR = 102;
    /**
     * User params
     */
    String USERINFO = "userinfo";
    String USERNM = "USERNM";
    String GENDER = "GENDER";
    String MPHONENO1 = "MPHONENO1";
    String MPHONENO2 = "MPHONENO2";
    String MPHONENO3 = "MPHONENO3";
    String USERID = "USERID";
    String LOW_USERID = "userID";
    String USERPW = "USERPW";
    String USERNUM = "USERNUM";
    String HEIGHT = "HEIGHT";
    String WEIGHT = "WEIGHT";
    String USEREML = "USEREML";
    String USERBTDATE = "USERBTDATE";

    /**
     * BPList params
     */
    String BPMSTYPE = "BPMSTYPE";
    String BPMSTYPE_T = "T"; //All
    String BPMSTYPE_D = "D"; //Equitment measurement
    String BPMSTYPE_U = "U"; //User input
    String BPLIST = "bpList";
    String BSLIST = "bsList";
    String LOW_WEIGHT = "weightList";
    String BPSEQ = "BPSEQ";
    String BPSYS = "BPSYS";
    String BPMIN = "BPMIN";
    String BPPULSE = "BPPULSE";
    String BPWEIGHT = "BPWEIGHT";
    String BPMEDICINEYN = "BPMEDICINEYN";
    String BPEXERCISEYN = "BPEXERCISEYN";
    String BPMSDATE = "BPMSDATE";
    int COMPLETE = 0; //of result code
    int FAIL = 1;    //of result code


    //PARAM SELFHEALTHMAIN
    String SELFHEALTHMAIN = "selfHealthMain";
    String HEALTHTYPE = "healthType";
    int GOOD = 1; //healthType
    int NORMALH = 2;//healthType
    int BAD = 3;//healthType
    String HEALTHADVICE = "healthAdvice";
    String HEALTHDATE = "healthDate";
    String BLOODSUGAR = "bloodSugar";
    String BSLEVELTYPE = "bsLevelType";
    String HIGHT = "H"; //bsLevelType-bpLevelType
    String LOW = "L"; //bsLevelType-bpLevelType
    String NORMAL = "N"; //bsLevelType-bpLevelType
    String SYSTOLICBP = "systolicBP";
    String DIASTOLICBP = "diastolicBP";
    String PULSE = "pulse";
    String BPLEVELTYPE = "bpLevelType";
    String BMI = "BMI";
    String BMR = "BMR";
    String GOALWEIGHT = "goalWeight";
    String GOALSTARTWEIGHT = "goalStartWeight";
    String STANDARWEIGHT = "standardWeight";
    String NOTICE = "NOTICE";
    String HEALTHCONDITION = "HEALTHCONDITION";

    //Param Measuring Information Last Input Obtaining Values
    String BSVAL = "BSVAL";
    String BSWEIGHT = "BSWEIGHT";
    String BSTYPE = "BSTYPE";
    String BSTYPE_B = "B";
    String BSTYPE_A = "A";
    String BSMSTYPE = "BSMSTYPE";
    String BSMEDICINEYN = "BSMEDICINEYN";
    String BSMEDICINEYN_Y = "Y";
    String BSMEDICINEYN_N = "N";
    String BSEXERCISEYN = "BSEXERCISEYN";
    String BSEXERCISEYN_Y = "Y";
    String BSEXERCISEYN_N = "N";
    String BSMSDATE = "BSMSDATE";
    String WTMSDATE = "WTMSDATE";
    String MSDATE = "MSDATE";
    String BSMSTYPE_T = "T"; //All
    String BSMSTYPE_D = "D"; //Equitment measurement
    String BSMSTYPE_U = "U"; //User input

    //Param Blood pressure history


    //Param Blood pressure history 2
    String STARTDATE = "STARTDATE";
    String ENDDATE = "ENDDATE";

    //Param Blood sugar history
    String BSSEQ = "BSSEQ";

    //Param	Weight history
    String WEIGHTSEQ = "WEIGHTSEQ";

    //Param	Enter your target weight
    String FITWEIGHT = "FITWEIGHT";
    String STARTFITWEIGHT = "STARTFITWEIGHT";

    //Param Update APK Download URL
    String APPURL = "APPURL";

    //Param  Document input
    String SMOKING = "SMOKING";
    String LIFE = "LIFE";
    String MEAT = "MEAT";
    String FRUIT = "FRUIT";
    String SLEEP = "SLEEP";
    String DRINKING = "DRINKING";
    String BLOODP = "BLOODP";
    String DIABETES = "DIABETES";
    String EXERCISE = "EXERCISE";
    String COLD = "COLD";
    String PERSONALITY = "PERSONALITY";
    String TENSION = "TENSION";
    String FITAGE = "FITAGE"; //output

    //Param Contents of paperwork
    String HEALTHQSEQ = "HEALTHQSEQ";

    //Health Quest
    String HEALTH_QUEST = "healthquest";

    //Home
    String LAST_MEASUREMENT = "lastMeasurement";
    String LOGIN_FAIL_MESSAGE = "로그인정보가 틀립니다.";
    String WITHDRAW_ACCOUNT_MESSAGE = "탈퇴 회원입니다.";

    //Param check member withdraw
    String WITHDRAW="WITHDRAW";
    String WITHDRAW_Y="Y";
}
