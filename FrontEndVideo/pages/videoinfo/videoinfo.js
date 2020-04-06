var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    cover: "cover",
    videoId:"",
    src: "",
    videoInfo: {},

  },

  videoCtx: {},

  onLoad: function (params) {

    var me = this;
    me.videoCtx = wx.createVideoContext('myVideo', me);
    // 获取前面一个页面传过来的videoInfo
    var videoInfo = JSON.parse(params.videoInfo);
    var videoHeight = videoInfo.videoHeight;
    var videoWidth = videoInfo.videoWidth;
    var cover = "cover";
    if (videoWidth>=videoHeight){
      cover = "";
    }

    me.setData({
      videoId: videoInfo.id,
      src: app.serverUrl + videoInfo.videoPath,
      videoInfo: videoInfo,
      cover: cover
    })


  },

  onShow: function () {
    var me = this;
    me.videoCtx.play();



  },

  onHide: function () {
    var me = this;
    me.videoCtx.pause();


  },

  showSearch: function () {

    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })

  },

  showPublisher: function () {
  },


  upload: function () {
    // 保存重定向的信息, 如果用户信息为空, 跳转回来
    var me = this;
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoinfo/videoinfo#videoInfo@'+ videoInfo;
    var userInfo = app.getGlobalUserInfo();
    // 如果用户信息为空(未登录), 直接跳转到登录页面
    if (userInfo==null || userInfo==undefined || userInfo==''){
      // debugger;
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      videoUtil.uploadVideo();
    }
  },

  showIndex: function () {
    wx.redirectTo({
      url: '../index/index',
    })
  },

  showMine: function () {
    var userInfo = app.getGlobalUserInfo();
    // 如果用户信息为空(未登录), 直接跳转到登录页面
    if (userInfo==null || userInfo==undefined || userInfo==''){
      wx.navigateTo({
        url: '../userLogin/login',
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine',
      })
    }

  },

  likeVideoOrNot: function () {
  },

  shareMe: function () {
  },

  onShareAppMessage: function (res) {
  },


  leaveComment: function () {
  },

  replyFocus: function (e) {
  },

  saveComment: function (e) {
  },


  getCommentsList: function (page) {
  },

  onReachBottom: function () {
  }
})