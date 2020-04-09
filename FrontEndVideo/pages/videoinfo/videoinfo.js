var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    cover: "cover",
    videoId: "",
    src: "",
    videoInfo: {},
    userLikeVideo: false,
    publisher: {},
    serverUrl: app.serverUrl
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
    if (videoWidth >= videoHeight) {
      cover = "";
    }

    me.setData({
      videoId: videoInfo.id,
      src: app.serverUrl + videoInfo.videoPath,
      videoInfo: videoInfo,
      cover: cover
    })

    // 调用后端接口查询视频的上传者信息+当前用户的点赞信息
    // 参数 loginUserId, videoId, publishUserId
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    var loginUserId = "";
    var videoId = videoInfo.id;
    var publishUserId = videoInfo.userId;
    if (user!=null && user!='' && user!=undefined){
      loginUserId = user.id;
    }

    wx.showLoading({
      title: '...',
    });
    wx.request({
      url: serverUrl + '/user/queryPublisher?loginUserId=' + loginUserId +'&videoId=' + videoId + '&publishUserId=' + publishUserId,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
      },
      success: function (res) {
        console.log(res.data);
        var realLikeVideo = res.data.data.userLikeVideo;
        var realpublisher = res.data.data.publisher;
        wx.hideLoading();
        if (res.data.status == 200) {
          me.setData({
            userLikeVideo: realLikeVideo,
            publisher: realpublisher
          })
        } 
        // else if (res.data.status == 502) {
        //   wx.showToast({
        //     title: res.data.msg,
        //     duration: 2000,
        //     icon: "none",
        //     success: function () {
        //       wx.redirectTo({
        //         url: '../userLogin/login',
        //       })
        //     }
        //   });
        // }
      },
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

    // 保存重定向的信息, 如果用户信息为空, 跳转回来
    var me = this;
    var videoInfo = me.data.videoInfo;
    var realUrl = '../mine/mine#publisherId@' + videoInfo.userId;
    var userInfo = app.getGlobalUserInfo();
    // 如果用户信息为空(未登录), 直接跳转到登录页面
    if (userInfo == null || userInfo == undefined || userInfo == '') {
      // debugger;
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine?publisherId=' + videoInfo.userId,
      })
    }
  },


  upload: function () {
    // 保存重定向的信息, 如果用户信息为空, 跳转回来
    var me = this;
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;
    var userInfo = app.getGlobalUserInfo();
    // 如果用户信息为空(未登录), 直接跳转到登录页面
    if (userInfo == null || userInfo == undefined || userInfo == '') {
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
    if (userInfo == null || userInfo == undefined || userInfo == '') {
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
    var me = this;

    var userInfo = app.getGlobalUserInfo();
    var userLikeVideo = me.data.userLikeVideo;
    var serverUrl = app.serverUrl;

    // 后端三个参数: userId, videoId, videoCreaterId
    var userId = userInfo.id;
    var videoId = me.data.videoInfo.id;
    var videoCreaterId = me.data.videoInfo.userId;
    var url = "";
    if (userLikeVideo == false) {
      url = serverUrl + '/video/userLike?userId=' + userId + '&videoId=' + videoId + '&videoCreaterId=' + videoCreaterId;
    } else {
      url = serverUrl + '/video/userUnLike?userId=' + userId + '&videoId=' + videoId + '&videoCreaterId=' + videoCreaterId;
    }

    wx.showLoading({
      title: '...',
    });
    // debugger;
    wx.request({
      url: url,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': userInfo.id,
        'userToken': userInfo.userToken
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          me.setData({
            userLikeVideo: !userLikeVideo,
          })
        } else if (res.data.status == 502) {
          wx.showToast({
            title: res.data.msg,
            duration: 2000,
            icon: "none",
            success: function () {
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          });
        }
      }
    })


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