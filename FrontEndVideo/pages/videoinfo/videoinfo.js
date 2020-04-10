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
    serverUrl: app.serverUrl,

    // 评论相关
    placeholder: '说点什么...'
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
    var me = this;
    wx.showActionSheet({
      itemList: ['下载到本地','举报用户','分享到朋友圈','分享到QQ空间','分享到微博'],
      success:function(res){
        var idx = res.tapIndex;
        if (idx == 0){
          //下载
          wx.showLoading({
            title: '下载中...',
          })
          wx.downloadFile({
            url: app.serverUrl + me.data.videoInfo.videoPath, //仅为示例，并非真实的资源
            success (res) {
              // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
              if (res.statusCode === 200) {
                console.log(res.tempFilePath);
                wx.saveVideoToPhotosAlbum({
                  filePath: res.tempFilePath,
                  success (res) {
                    console.log(res.errMsg);
                    wx.hideLoading();
                    wx.showToast({
                      title: '下载成功',
                      icon: 'success',
                      duration: 2000
                    })
                  }
                })
              }
            }
          })
        } else if (idx == 1){
          //举报
          // 若用户未登录，进行拦截并重定向回来
          var videoInfo = JSON.stringify(me.data.videoInfo);
          var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;
          var userInfo = app.getGlobalUserInfo();
          // 如果用户信息为空(未登录), 直接跳转到登录页面
          if (userInfo == null || userInfo == undefined || userInfo == '') {
            wx.navigateTo({
              url: '../userLogin/login?redirectUrl=' + realUrl,
            })
          } else {
            var videoId = me.data.videoInfo.id;
            var publisherId = me.data.videoInfo.userId;
            wx.navigateTo({
              url: '../report/report?videoId=' + videoId + '&publisherId=' + publisherId,
            })
          }
        } else {
          // 其他Tab
          wx.showToast({
            title: '官方暂未开放...',
          })
        }
      }
    })


  },

  onShareAppMessage: function (res) {
    var me = this;
    var videoInfo = JSON.stringify(me.data.videoInfo);
    return {
      title: '短视频内容转发',
      path: "pages/videoinfo/videoinfo?videoInfo=" + videoInfo,
    }

  },


  leaveComment: function () {

    this.setData({
      commentFocus: true,
    })

  },

  replyFocus: function (e) {
  },

  saveComment: function (e) {
    var me = this;
    var userInfo = app.getGlobalUserInfo();

    // 若用户未登录，进行拦截并重定向回来
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoinfo/videoinfo#videoInfo@' + videoInfo;
    var userInfo = app.getGlobalUserInfo();
    // 如果用户信息为空(未登录), 直接跳转到登录页面
    if (userInfo == null || userInfo == undefined || userInfo == '') {
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    } else {
      var comment = e.detail.value;
      var videoId = me.data.videoInfo.id;
      var fromUserId = userInfo.id;
      var serverUrl = app.serverUrl;

      wx.showLoading({
        title: '请稍等...',
      })
      wx.request({
        url: serverUrl + '/video/saveComment',
        method: "POST",
        header: {
          'content-type': 'application/json', // 默认值
          'userId': userInfo.id,
          'userToken': userInfo.userToken
        },
        data: {
          comment: comment,
          videoId: videoId,
          fromUserId: fromUserId,
        },
        success: function(res){
          console.log(res.data);
          wx.hideLoading();
          me.setData({
            contentValue: "",
          })
        }
      })

    }



   

  },


  getCommentsList: function (page) {
  },

  onReachBottom: function () {
  }
})