package com.clinical.tongxin.util;

import static com.xiaomi.push.service.y.B;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class UrlUtils {
    //http://192.168.0.237:8057/Site/witnesses

//    public static final String BASE_URL = "http://123.57.227.224:8088/";//内网http://115.29.42.75:8088/

//        public static final String BASE_URL = "http://118.190.24.11:8888/";
//    public static final String BASE_URL = "http://59.110.63.236/";
    public static final String BASE_URL = "http://59.110.63.236:8080/";
    public static final String BASE_URL1 = "http://59.110.63.236:80";
//    public static final String BASE_URL = "http://59.110.63.236:8080/";
//    public static final String BASE_URL = "http://192.168.1.96:8080/";

    //apk下载地址
//    public static final String URL_APK = "http://59.110.63.236:8080/App/txAndroid.apk";
//    public static final String URL_APK = "http://59.110.63.236/App/txAndroid.apk";
//    public static final String URL_APK = "http://192.168.3.96:8080/App/txAndroid.apk";
    public static final String URL_APK = "http://59.110.63.236:80/App/txAndroid.apk";
    //获取省份
    public static final String URL_Province = BASE_URL + "inter/provinceInterface.ashx?func=list";
    //获取城市
    public static final String URL_City = BASE_URL + "inter/cityInterface.ashx?func=list";
    //获取注册验证码
    public static final String URL_GetVerificationCode = BASE_URL + "register.do?action=ShowSendMessageCode";
    //登录验证码
    public static final String URL_GetLoginVerificationCode = BASE_URL + "register.do?action=ShowSendMessageCodeByLogin";

    //用户登录
    public static final String URL_UserLogin = BASE_URL + "quser.do?action=UserLogInApp";
    //用户注册
    public static final String URL_Create = BASE_URL + "register.do?action=SaveRegisterApp";
    //确认验证码
    public static final String URL_CheckCode = BASE_URL + "inter/SmsVerificationCodeP.ashx?func=CheckCode";
    //用户协议
    public static final String URl_GetAgreement = BASE_URL + "register.do?action=ShowRegistrationagreementApp";
    //开户银行
    public static final String URL_BankInterface = BASE_URL + "bank.do?action=ShowBank_yhApp";
    //获取开户支行
    public static final String URL_BankBranchInterface = BASE_URL + "bankbranch.do?action=ShowBankBranch_zhApp";
    //实名认证
    public static final String URL_IdentityAuthentication = BASE_URL + "quser.do?action=CheckIDCardApp";
    //设置支付方式
    public static final String URL_PaySetting = BASE_URL + "quser.do?action=SetPayMessageApp";//
    //获取失败yuanyin
    public static final String URL_GetNotAuthenticationReason = BASE_URL + "quser.do?action=ShowBackAppReason";
    //填写备注码
    public static final String URL_FillAuthenticationCode = BASE_URL + "quser.do?action=CheckIDCardAndMoney";
    //找回密码
    public static final String URL_ShowFindpasswordApp = BASE_URL + "register.do?action=ShowFindpasswordApp";
    //获取找回密码的验证码
    public static final String URL_ShowSendMessageCodeByFindpassword = BASE_URL + "register.do?action=ShowSendMessageCodeByFindpassword";
    //获取实名认证信息
    public static final String URL_GetrealCustomerMessage = BASE_URL + "customer.do?action=GetrealCustomerMessage";
    //获取资产余额
    public static final String URl_ShowAppDirectororg=BASE_URL+"dirbud.do?action=ShowAppDirectororg";
    //预算情况
    public static final String URL_ShowAppBudget=BASE_URL+"dirbud.do?action=ShowAppBudget";
    //花费金额
    public static final String ShowAppMonthCost=BASE_URL+"task.do?action=ShowAppMonthCost";
    //任务统计
    public static final String URl_ShowCountTask=BASE_URL+"taskapp.do?action=ShowCountTask";
    //任务类型
    public static final String URL_ShowTaskType=BASE_URL+"taskapp.do?action=ShowTaskType";
    //我的资产列表
    public static final String URl_getMyBalanceDetails_app=BASE_URL+"balanceDetail.do?action=getMyBalanceDetails_app";
    //资产充值详情
    public static final String URL_getBalanceDetailApp=BASE_URL+"balanceDetail.do?action=getBalanceDetailApp";
    //资产任务详情
        public static final String URL_queryOrdersInfoApp=BASE_URL+"balanceDetail.do?action=queryOrdersInfoApp";
    //总监充值
    public static final String URL_apprecharge=BASE_URL+"diectororg.do?action=apprecharge";
    //接包人充值
    public static final String URL_rechargeApp=BASE_URL+"balanceDetail.do?action=rechargeApp";
    //接包人提现
    public static final String URL_applyMoneyApp=BASE_URL+"balanceDetail.do?action=applyMoneyApp";
    //分配预算列表
    public static final String URL_ShowAppNowDateDirBud=BASE_URL+"dirbud.do?action=ShowAppNowDateDirBud";
    //总监预算额
    public static final String URL_queryNowDateAmountApp=BASE_URL+"dirbud.do?action=queryNowDateAmountApp";
    //分配预算调整
    public static final String URL_setAppNowDateAmount=BASE_URL+"dirbud.do?action=setAppNowDateAmount";
    //预算查询
    public static final String URl_ShowAppBudgetList=BASE_URL+"dirbud.do?action=ShowAppBudgetList";
    //群组列表
    public static final String URL_ShowAllAppgroup=BASE_URL+"appgroup.do?action=ShowAllAppgroup";
    //删除群组
    public static final String URL_delAppgroup=BASE_URL+"appgroup.do?action=delAppgroup";
    //群组成员列表
    public static final String URl_ShowAllAppgroupCustomer=BASE_URL+"appgroup.do?action=ShowAllAppgroupCustomer";
    //获取添加成员
    public static final String URL_AddAppgroupCustomer=BASE_URL+"appgroup.do?action=AddAppgroupCustomer";
    //获取添加成员列表
    public static final String URL_ShowMayAddCustomer=BASE_URL+"appgroup.do?action=ShowMayAddCustomer";
    //添加群组
    public static final String URL_AddAppgroup=BASE_URL+"appgroup.do?action=AddAppgroup";
    //删除群组成员
    public static final String URL_DelAppgroupCustomer=BASE_URL+"appgroup.do?action=DelAppgroupCustomer";
    //经理预算查询
    public static final String URL_ShowAppMyBudgetList=BASE_URL+"dirbud.do?action=ShowAppMyBudgetList";
    //经理预算详情
    public static final String URl_ShowAppPMNowDateAmount=BASE_URL+"dirbud.do?action=ShowAppPMNowDateAmount";


    // 创建团队
    public static final String URL_TEAM_CREATE = BASE_URL + "directorAppGroup.do?action=AddMyDirectorGroup";
    // 删除团队
    public static final String URL_TEAM_DEL = BASE_URL + "directorAppGroup.do?action=DelMyDirectorGroup";
    // 编辑团队
    public static final String URL_TEAM_UPDATE = BASE_URL + "directorAppGroup.do?action=UpdateMyDirectorGroup";
    // 获取团队列表
    public static final String URL_TEAM_GET_LIST = BASE_URL + "directorAppGroup.do?action=ShowMyDirectorGroup";
    // 经理获取成员列表
    public static final String URL_MANAGER_GET_LIST = BASE_URL + "pmAppCustomerGroup.do?action=ShowMyPMCustomerGroup";
    // 经理添加成员
    public static final String URL_MANAGER_ADD_MEMBER = BASE_URL + "pmAppCustomerGroup.do?action=AddMyPMCustomerGroup";
    // 经理删除人员
    public static final String URL_MANAGER_DEL_MEMBER = BASE_URL + "pmAppCustomerGroup.do?action=DelMyPMCustomerGroup";
    // 获取项目类型
    public static final String URL_PROTECT_TYPE = BASE_URL + "tasktype.do?action=queryProjectTypeName";
    // 获取任务类型
    public static final String URL_TASK_TYPE = BASE_URL + "tasktype.do?action=getAll";
    // 获取用户列表（添加团队成员）
    public static final String URL_GET_USER_LIST = BASE_URL + "directorAppGroup.do?action=ShowThreeGroup";
    // 接包人 删除人员
    public static final String URL_DEL_CONTRACTOR_MEMBER = BASE_URL + "customerAppGroup.do?action=DelMyCustomerGroup";
    // 接包人 添加人员
    public static final String URL_ADD_CONTRACTOR_MEMBER = BASE_URL + "customerAppGroup.do?action=AddMyCustomerGroup";
    // 接包人 获取团队列表
    public static final String URL_GET_CONTRACTOR_MEMBER = BASE_URL + "customerAppGroup.do?action=ShowMyCustomerGroup";
    // 获取任务列表
    public static final String URL_GET_TASK_LIST = BASE_URL + "taskapp.do?action=ShowTaskList";
    // 获取公共全部任务列表
    public static final String URL_querySingleAllTaskList = BASE_URL + "taskapp.do?action=querySingleAllTaskList";
    // 获取公共附近任务列表
    public static final String URL_ShowTaskjwList = BASE_URL + "taskapp.do?action=ShowTaskjwList";
    // 获取公共附竞价中任务列表
    public static final String URL_queryAllBiddingTask = BASE_URL + "taskapp.do?action=queryAllBiddingTask";
    // 获取任务详情
    public static final String URL_SHOW_TASK_INFO= BASE_URL + "taskapp.do?action=ShowTaskInfo";
    // 竞价
    public static final String URL_SHOW_AUCTION= BASE_URL + "taskbidcm.do?action=AppParticipateBidding";
    // 评价提交
    public static final String URL_TASK_EVALUATE= BASE_URL + "taskreview.do?action=ShowTaskReviewApp";
    // 评价提交
    public static final String URL_GET_QUESTION= BASE_URL + "taskreviewitem.do?action=queryTaskreviewitemApp";
    // 修改密码
    public static final String URL_AppUpdateLoginPwd= BASE_URL + "quser.do?action=AppUpdateLoginPwd";
    // 修改用户名
    public static final String URL_AppUpdateMobile= BASE_URL + "quser.do?action=AppUpdateMobile";
    //获取修用户名的验证码
    public static final String URL_AppSendCodeOfUpdateMobile=BASE_URL+"quser.do?action=AppSendCodeOfUpdateMobile";

    // 查询昵称
    public static final String URL_GET_NICKNAME= BASE_URL + "appgroup.do?action=AppGetNickNameByCusomeerId";

    // 项目经理我的资产
    public static final String URL_GET_BUDGET= BASE_URL + "appManagerBudget.do?action=showAppPMNowDateAmount";
    // 提交任务
    public static final String URL_COMMIT= BASE_URL + "taskapp.do?action=taskRelease";
    // 竞价信息
    public static final String URL_AUCTION_INFO= BASE_URL + "taskapp.do?action=showTaskOver";
    // 结束竞价
    public static final String URL_END_AUCTION= BASE_URL + "taskapp.do?action=endTaskOfbid";
    // 支付给接包人
    public static final String URL_PAY_TASK= BASE_URL + "taskapp.do?action=payTask";
    // 接包人接受或拒绝任务
    public static final String URL_TASK_RECEIVE= BASE_URL + "taskapp.do?action=receiveTaskManagement";
    // 审批
    public static final String URL_TASK_APPROVAL= BASE_URL + "taskapp.do?action=taskApproval";
    // 提交反馈
    public static final String URL_saveCustomerQutstionApp= BASE_URL + "customer.do?action=saveCustomerQutstionApp";
    //消息验证（同意）
    public static final String URL_receiveInvitation=BASE_URL+"directorAppGroup.do?action=receiveInvitation";
    // 仲裁信息
    public static final String URL_queryTaskArbitrateInfo= BASE_URL + "taskapp.do?action=queryTaskArbitrateInfo";




    //获取app最新版本
    public static final String URL_GetAppNewVersion = BASE_URL + "appver.do?action=ShowAppver";

    //获取首页提示数
    public static final String URL_GetHomePormptNum = BASE_URL + "Site/getHomePormptNum";
    //首页Banner
    public static final String URL_FindBanner = BASE_URL + "imageShow.do?action=ImageShow";
    //首页中国好整形banner
    public static final String URL_FindGoodPlastic = BASE_URL + "/Site/findGoodPlastic";
    //添加委托担保
    public static final String URL_AuthorizedGuaranteeOrder = BASE_URL + "Mirror/authorizedGuaranteeOrder";
    //获取委托担保列表
    public static final String URL_FindGuaranteeOrder = BASE_URL + "My/getPolicyList";
    //保单修改
    public static final String URL_PolicyEdit = BASE_URL + "My/PolicyEdit";
    //获取需求详情
    public static final String URL_GetDemandDetails = BASE_URL + "Common/getDemandDetails";
    //打假或服务举报
    public static final String URL_Witnesses = BASE_URL + "Site/witnesses";
    //验证医生
    public static final String URL_VerifyDoctor = BASE_URL + "Doctor/verifyDoctor";
    //获取项目分类
    public static final String URL_FindClassify = BASE_URL + "Common/findClassify";
    //获取省份列表
    public static final String URL_FindProvince = BASE_URL + "Common/findProvince";
    //申请会诊
    public static final String URL_ApplyConsultation = BASE_URL + "Mirror/applyConsultation";
    //手术失败修复
    public static final String URL_ApplyFailRepair = BASE_URL + "Mirror/applyFailRepair";
    //公益会诊
    public static final String URL_PublicWelfareConsultation = BASE_URL + "Mirror/publicWelfareConsultation";
    //纠纷调节
    public static final String URL_DisputeResolve = BASE_URL + "Mirror/disputeResolve";
    //添加需求
    public static final String URL_ReleaseDemand = BASE_URL + "Beauty/releaseDemand";
    //验证机构
    public static final String URl_VerifyAgency = BASE_URL + "Doctor/verifyAgency";
    //验证耗材
    public static final String URL_VerifyProduce = BASE_URL + "Doctor/verifyProduce";
    //获取耗材类别列表
    public static final String URl_FindProduceType = BASE_URL + "Common/findProduceType";
    //获取专家列表
    public static final String URl_FindExpert = BASE_URL + "Common/findExpert";
    //获取机构列表
    public static final String URl_FindAgency = BASE_URL + "Common/findAgency";
    //向专家发布疑问
    public static final String URl_ReleaseQuestions = BASE_URL + "Doctor/releaseQuestions";
    //获取项目拍卖列表
    public static final String URl_FindAuctionProject = BASE_URL + "Beauty/findAuctionProject";
    //查询病历
    public static final String URL_QueryMedicalRecord = BASE_URL + "Doctor/queryMedicalRecord";
    //获取我的疑问列表
    public static final String URL_FindProblems = BASE_URL + "Doctor/findProblems";
    //获取同类问题列表
    public static final String URL_FindQueries = BASE_URL + "My/findQueries";
    //取消订单
    public static final String URL_CancelOrder = BASE_URL + "Common/cancelOrder";
    //订单支付
    public static final String URL_PayOrder = BASE_URL + "Common/payOrder";
    //订单申请退款
    public static final String URL_ApplyRefunds = BASE_URL + "My/applyRefunds";
    //订单申请调解
    public static final String URL_ApplyResolve = BASE_URL + "My/applyResolve";
    //获取订单列表
    public static final String URL_FindOrder = BASE_URL + "Common/findOrder";
    //获取我的纠纷列表
    public static final String URL_FindMyDispute = BASE_URL + "My/findMyDispute";
    //机构提交平台验证1
    public static final String URL_IndustrySubmitPlam = BASE_URL + "Doctor/IndustrySubmitPlam";
    //耗材提交平台验证
    public static final String URL_ProductSubmitPlam = BASE_URL + "Doctor/ProductSubmitPlam";
    //修改个人信息
    public static final String URL_InfoEdit = BASE_URL + "My/InfoEdit";
    //获取我的竞拍
    public static final String URL_FindMyAuction = BASE_URL + "My/findMyAuction";
    //医生提交平台验证
    public static final String URL_DoctorSubmitPlam = BASE_URL + "Doctor/DoctorSubmitPlam";
    //获取保单列表
    public static final String URl_FindPolicy = BASE_URL + "Site/findPolicy";
    //获取视频列表
    public static final String URL_FindVideo = BASE_URL + "Common/findVideo";
    //获取热门竞拍
    public static final String URL_FindHotAuction = BASE_URL + "Common/findHotAuction";
    //获取医生详情
    public static final String URL_GetDoctorDetails = BASE_URL + "Common/getDoctorDetails";
    //获取项目详情
    public static final String URL_GetProjectDetails = BASE_URL + "Common/getProjectDetails";
    //项目下单
    public static final String URL_GenerateOrder = BASE_URL + "Common/generateOrder";
    //获取机构详情
    public static final String URL_GetAgencyDetails = BASE_URL + "Common/getAgencyDetails";
    //案例展示列表
    public static final String URL_GetCaseList = BASE_URL + "Common/getCaseList";
    //案例点击增加点击量
    public static final String URL_AddCaseView = BASE_URL + "Common/AddCaseView";
    //增加视频点击量
    public static final String URL_AddVideoView = BASE_URL + "Common/AddVideoView";
    //参与竞拍
    public static final String URL_JoinAuction = BASE_URL + "Common/JoinAuction";
    //一口价
    public static final String URL_FixedAuction = BASE_URL + "Common/FixedAuction";
    //获取展示图片
    public static final String URL_FindDisplayInfo = BASE_URL + "Common/findDisplayInfo";
    //特约专家
    public static final String URL_FindSpecial = BASE_URL + "Common/findSpecial";
    //获取项目剩余时间（竞拍）
    public static final String URL_FindProjectTime = BASE_URL + "Common/findProjectTime";
    //获取病历列表
    public static final String URL_MyMedicalRecordList = BASE_URL + "My/myMedicalRecordList";
    //获取病历详情
    public static final String URL_MyMedicalRecordDetails = BASE_URL + "My/myMedicalRecordDetails";
    //获取环信用户信息
    public static final String URL_GetHXDetails = BASE_URL + "Common/getHXDetails";
    //疑问评价
    public static final String URL_QueriesEvaluate = BASE_URL + "My/queriesEvaluate";
    //获取我的需求
    public static final String URL_FindMyDemand = BASE_URL + "My/findMyDemand";
    //需求撤销
    public static final String URL_DemandRevoke = BASE_URL + "My/DemandRevoke";
    //个人信息统计
    public static final String URL_MyInfoDetails = BASE_URL + "My/myInfoDetails";
    //获取组织机构列表 非接包人
    public static final String URL_queryDirectorGroup = BASE_URL + "directorAppGroup.do?action=queryDirectorGroup";
    //获取团队信息
    public static final String URL_queryDirectorGroupByManagerCustomerId = BASE_URL + "directorAppGroup.do?action=queryDirectorGroupByManagerCustomerId";
    //获取组织机构列表 接包人
    public static final String URL_queryCustomerGroup = BASE_URL + "customerAppGroup.do?action=queryCustomerGroup";
    //上传头像
    public static final String URL_updatePhotourlApp = BASE_URL + "quser.do?action=updatePhotourlApp";

    //获取任务列表 二期 接包人
    public static final String URL_queryReceiverSubscribeTaskList = BASE_URL + "taskapp.do?action=queryReceiverSubscribeTaskList";
    //获取任务详情 二期 接包人
    public static final String URL_ShowTaskInfo= BASE_URL + "taskapp.do?action=ShowTaskInfo";
    //获取评价列表 二期
    public static final String URL_queryTaskAllReview= BASE_URL + "taskapp.do?action=queryTaskAllReview";
    //获取任务列表 二期 发包人
    public static final String URL_queryCreateTaskList= BASE_URL + "taskapp.do?action=queryCreateTaskList";
    //获取任务详情 二期 发包人
    public static final String URL_queryCreateTaskInfo= BASE_URL + "taskapp.do?action=queryCreateTaskInfo";
    //任务查看计数 二期
    public static final String URL_recordAccessCount= BASE_URL + "recordAccess.do?action=recordAccessCountApp";
    //任务查看计数 二期
    public static final String URL_endTaskOfbidByPerson= BASE_URL + "taskapp.do?action=endTaskOfbidByPerson";
    //获取订阅列表 二期
    public static final String URL_queryAppSubscriptionCount= BASE_URL + "subscription.do?action=queryAppSubscriptionCount";
    //订阅城市 二期
    public static final String URL_saveAppAreaSubscription= BASE_URL + "subscription.do?action=saveAppAreaSubscription";
    //订阅城市 二期
    public static final String URL_saveAppTaskTypeSubscription= BASE_URL + "subscription.do?action=saveAppTaskTypeSubscription";
    //任务忽略 二期
    public static final String URL_taskNeglect= BASE_URL + "taskapp.do?action=taskNeglect";
    //任务状态数量 二期
    public static final String URL_ShowListTaskCount= BASE_URL + "taskapp.do?action=ShowListTaskCount";
    // 仲裁题 二期
    public static final String URL_queryTaskarbitrateitemApp = BASE_URL+"taskarbitem.do?action=queryTaskarbitrateitemApp";
    // 仲裁 二期
    public static final String URL_ShowTaskArbitrate = BASE_URL+"taskapp.do?action=ShowTaskArbitrate";
    // 任务验收详情 二期
    public static final String URL_getAcceptanceRatio = BASE_URL+"task.do?action=getAcceptanceRatio";
    // 任务终止 二期
    public static final String URL_endTask = BASE_URL+"taskapp.do?action=endTask";
    // 服务 二期
    public static final String URL_queryServiceNavigationList = BASE_URL+"serviceNavigationManagement.do?action=queryServiceNavigationList";
    // 工程资质 二期
    public static final String URL_queryQualifications = BASE_URL+"subscription.do?action=queryQualifications";
    // 查询评价问题 二期
    public static final String URL_queryTaskreviewitemApp = BASE_URL+"taskreviewitem.do?action=queryTaskreviewitemApp";
    // 查询余额 二期
    public static final String URL_queryCustomer = BASE_URL+"quser.do?action=queryCustomer";

    // 用户实名验证后个人资质评估 二期
    public static final String URL_saveQualificationByCustomerApp = BASE_URL+"customer.do?action=saveQualificationByCustomerApp";
    // 我能提供的资质 查询任务 二期
    public static final String URL_queryAllTaskType = BASE_URL+"customertasktype.do?action=queryAllTaskType";
    // 我能提供的资质 查询项目 二期
    public static final String URL_queryAllProjecttype = BASE_URL+"customerprojecttype.do?action=queryAllProjecttype";
    // 查询推广人信息
    public static final String URL_queryPromoteMobielCheck = BASE_URL+"register.do?action=queryPromoteMobielCheck";
    //全部培训视频
    public static final String URL_TRAIN_VIDEO_ALL = BASE_URL + "videoType.do?action=queryVideoListApp";
    //按视频类型分组
    public static final String URL_TRAIN_VIDEO_TYPE = BASE_URL + "videoType.do?action=queryVideoListChickApp";
    //某视频类型全部视频
    public static final String URL_TRAIN_TYPE_VIDEO = BASE_URL + "videoType.do?action=queryVideoListAllApp";
    //视频点击量
    public static final String URL_TRAIN_VIDEO_CLICK_TIMES_ADD = BASE_URL + "videoType.do?action=updateVideoChickCount";
    public static final String URL_RESUME = BASE_URL + "resume.do?action=showResume";
    public static final String URL_POST_RESUME_HEAD_PHOTO = BASE_URL + "resume.do?action=picSave";
    public static final String URL_POST_RESUME_BASIC_INFO = BASE_URL + "resume.do?action=updateResumeBasic";
    public static final String URL_POST_RESUME_WORK_EXPERIENCE = BASE_URL + "resume.do?action=saveResumeWork";
    public static final String URL_POST_RESUME_PROJECT_EXPERIENCE = BASE_URL + "resume.do?action=saveResumeProject";
    public static final String URL_POST_RESUME_EDUCATION_EXPERIENCE = BASE_URL + "resume.do?action=saveResumeEducation";
    public static final String URL_POST_RESUME_CERTIFICATE = BASE_URL + "resume.do?action=saveResumeCertificate";
    public static final String URL_POST_RESUME_SKILL = BASE_URL + "resume.do?action=saveResumeSkills";
    public static final String URL_POST_RESUME_CERTIFICATE_ATTACHMENT = BASE_URL + "resume.do?action=saveResumeAttachment";
    public static final String URL_POST_RESUME_WORK_EXPERIENCE_DELETE = BASE_URL + "resume.do?action=deleteResumeWork";
    public static final String URL_POST_RESUME_PROJECT_EXPERIENCE_DELETE = BASE_URL + "resume.do?action=projectDelete";
    public static final String URL_POST_RESUME_EDUCATION_EXPERIENCE_DELETE = BASE_URL + "resume.do?action=deleteResumeEducation";
    public static final String URL_POST_RESUME_CERTIFICATE_DELETE = BASE_URL + "resume.do?action=certificateDelete";
    public static final String URL_POST_RESUME_SKILL_DELETE = BASE_URL + "resume.do?action=skillsDelete";
    public static final String URL_POST_RESUME_CERTIFICATE_ATTACHMENT_DELETE = BASE_URL + "resume.do?action=attachmentDelete";
    public static final String URL_GET_RESUME_BASIC_INFO = BASE_URL + "resume.do?action=getResume";
    public static final String URL_GET_RESUME_WORK_EXPERIENCE_LIST = BASE_URL + "resume.do?action=listResumeWork";
    public static final String URL_GET_RESUME_PROJECT_EXPERIENCE_LIST = BASE_URL + "resume.do?action=listResumeProject";
    public static final String URL_GET_RESUME_EDUCATION_EXPERIENCE_LIST = BASE_URL + "resume.do?action=listResumeEducation";
    public static final String URL_GET_RESUME_CERTIFICATE_LIST = BASE_URL + "resume.do?action=listResumeCertificate";
    public static final String URL_GET_RESUME_SKILL_LIST = BASE_URL + "resume.do?action=listResumeSkills";
    public static final String URL_GET_RESUME_CERTIFICATE_ATTACHMENT_LIST = BASE_URL + "resume.do?action=listResumeAttachment";
    // 租赁 找任务 任务详情 接包人
    public static final String URL_FIND_TASK_LEASE_DEATAIL = BASE_URL + "taskLeaseApp.do?action=queryReceiverTaskInfo";
    // 租赁 找任务 任务详情 我要竞价
    public static final String URL_ParticipateBidding = BASE_URL + "taskLeaseApp.do?action=ParticipateBidding";
    // 租赁 找任务 任务详情 发包人
    public static final String URL_FIND_TASK_LEASE_PUBLISHER_DEATAIL = BASE_URL + "taskLeaseApp.do?action=queryCreateTaskFindInfo";
    // 租赁  发包人 确认中标
    public static final String URL_LEASE_PUBLISHER_CONFIRM = BASE_URL + "taskLeaseApp.do?action=endTaskOfbidByPerson";
    // 租赁  做任务 任务列表
    public static final String URL_LEASE_DO_TASK_LIST = BASE_URL + "taskLeaseApp.do?action=queryTaskLeaseList";
    // 租赁  做任务 任务详情 发包方
    public static final String URL_LEASE_DO_TASK_DETAIL_PUBLISHER = BASE_URL + "taskLeaseApp.do?action=queryCreateTaskInfo";

    // 租赁  做任务 签到日期 接包方
    public static final String URL_queryTaskSignByPerson= BASE_URL + "taskLeaseApp.do?action=queryTaskSignByPerson";
    // 租赁  做任务 签到日期 发包方
    public static final String URL_queryTaskSignByCustomerId= BASE_URL + "taskLeaseApp.do?action=queryTaskSignByCustomerId";
    // 租赁  做任务 签到
    public static final String URL_updateTaskSignBySign= BASE_URL + "taskLeaseApp.do?action=updateTaskSignBySign";
    // 租赁  做任务 签到确认 发包方
    public static final String URL_updateTaskSignByConfirm= BASE_URL + "taskLeaseApp.do?action=updateTaskSignByConfirm";
    // 租赁  做任务 终止 发包方
    public static final String URL_endTaskLease= BASE_URL + "taskLeaseApp.do?action=endTaskLease";

    // 租赁  做任务 查询支付金额 发包方
    public static final String URL_queryTaskLeasePrice= BASE_URL + "taskLeaseApp.do?action=queryTaskLeasePrice";
    // 租赁  做任务 支付 发包方
    public static final String URL_payTaskLease= BASE_URL + "taskLeaseApp.do?action=payTaskLease";

    // 租赁  做任务 仲裁 发包方
    public static final String URL_arbitrationTaskLease= BASE_URL + "taskLeaseApp.do?action=arbitrationTaskLease";
    // 租赁  评价
    public static final String URL_reviewTaskLease= BASE_URL + "taskLeaseApp.do?action=reviewTaskLease";
    // 租赁  仲裁
    public static final String URL_ARBITRATE_INFO_LEASE= BASE_URL + "taskLeaseApp.do?action=queryTaskArbitrateInfo";

}

