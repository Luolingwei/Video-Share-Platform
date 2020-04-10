var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
    serverUrl: app.serverUrl,
    isMe: true,
    isFollow: false,
    publisherId: null,
    // 视频Tab样式
    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedLike: "",
    isSelectedFollow: "",

    myVideoList: [],
    myVideoPage: 1,
    myVideoTotal: 1,

    likeVideoList: [],
    likeVideoPage: 1,
    likeVideoTotal: 1,

    followVideoList: [],
    followVideoPage: 1,
    followVideoTotal: 1,

    myWorkFalg: false,
    myLikesFalg: true,
    myFollowFalg: true

  },

  onLoad: function (params) {
    var me = this;
    var publisherId = params.publisherId;
    // 修改原有的全局对象为本地缓存
    var user = app.getGlobalUserInfo();
    // isMe
    var userId = user.id;
    var fanId = "";
    // 如果从上一页面跳转过来，带了publisherId的参数，mine页面信息加载用publisherId
    if (publisherId != null && publisherId != '' && publisherId != undefined){
      // !isMe
      userId = publisherId;
      fanId = user.id;
      me.setData({
        isMe: false,
        publisherId: publisherId
      })
    }
    me.setData({
      userId: userId,
    })
  
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    wx.request({
      url: serverUrl + '/user/query?userId=' + userId + '&fanId=' + fanId,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': user.id,
        'userToken': user.userToken
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        // debugger;
        if (res.data.status == 200) {
          console.log(res.data.data);
          var userInfo = res.data.data;
          var faceurl = "../resource/images/noneface.png";
          if (userInfo.faceImage != null && userInfo.faceImage != '' && userInfo.faceImage != undefined) {
            faceurl = serverUrl + userInfo.faceImage;
          }
          me.setData({
            faceUrl: faceurl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname,
            isFollow: userInfo.follow
          })
        } else if (res.data.status == 502){
          wx.showToast({
            title: res.data.msg,
            duration: 3000,
            icon: "none",
            success: function(){
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          })

        }
      }
    });
    me.getMyVideoList(1);
  },

  followMe: function (e) {

    var me = this;
    // 后端两个参数 userId, fanId
    var user = app.getGlobalUserInfo();
    var fanId = user.id;
    var userId = me.data.publisherId;

    var serverUrl = app.serverUrl;
    var followType = e.currentTarget.dataset.followtype;
    var url = ""
    // 用户点击关注
    if (followType =='1'){
      url = serverUrl + "/user/beyourfans?userId=" + userId + "&fanId=" + fanId; 
    // 用户取消关注
    } else {
      url = serverUrl + "/user/dontbeyourfans?userId=" + userId + "&fanId=" + fanId; 
    }

    wx.showLoading();
    wx.request({
      url: url,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': user.id,
        'userToken': user.userToken
      },
      success:function(res){
        console.log(res);
        wx.hideLoading();
        // 用户点击关注
        if (followType =='1'){
          var isFollow = true;
          var fanschange = 1;
        // 用户取消关注
        } else {
          var isFollow = false;
          var fanschange = -1;
        }
        me.setData({
          isFollow: isFollow,
          fansCounts: me.data.fansCounts + fanschange,
        })
      }
    })

  },

  logout: function () {
    // var user = app.userInfo;
    // 修改原有的全局对象为本地缓存
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    wx.request({
      url: serverUrl + '/logout?userId=' + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          wx.showToast({
            title: "用户注销成功",
            icon: 'none',
            duration: 2000
          }),
            // app.userInfo = null;
            // 注销后清空缓存
          wx.removeStorageSync('userInfo');
          wx.navigateTo({
            url: '../userLogin/login',
          })
        }
      }
    })
  },

  changeFace: function () {
    var me = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album'],
      success(res) {
        // tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths;
        console.log(tempFilePaths);
        // var user = app.userInfo;
        // 修改原有的全局对象为本地缓存
        var user = app.getGlobalUserInfo();
        var serverUrl = app.serverUrl;
        wx.showLoading({
          title: '上传中',
        });
        wx.uploadFile({
          url: serverUrl + '/user/uploadFace?userId=' + user.id,
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type': 'application/json', // 默认值
            'userId': user.id,
            'userToken': user.userToken
          },
          success: function (res) {
            var data = JSON.parse(res.data);
            wx.hideLoading();
            console.log(data);
            // debugger;
            if (data.status == 200) {
              wx.showToast({
                title: '上传成功',
                icon: "success"
              })
              var imageUrl = data.data;
              me.setData({
                faceUrl: serverUrl + imageUrl
              });
            } else if (data.status == 500) {
              wx.showToast({
                title: '上传失败',
                icon: "fail"
              })
            } else if (data.status == 502) {
              wx.showToast({
                title: data.msg,
                duration: 2000,
                icon: "none",
                success: function () {
                  wx.redirectTo({
                    url: '../userLogin/login',
                  })
                }
              });
            }
            //do something
          }
        })
      }
    })
  },

  uploadVideo: function () {
    videoUtil.uploadVideo();
  },

  getMyVideoList: function(page){

    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中...',
    });

    wx.request({
      url: serverUrl + '/video/showAll?page=' + page,
      method: "POST",
      data: {
        userId: me.data.userId,
      },
      success: function (res) {
        wx.hideLoading();
        console.log(res.data);
        // 判断当前页page是否是第一页，如果是第一页，那么设置videoList为空 (为了下拉刷新设置)
        if (page === 1) {
          me.setData({
            myVideoList: []
          });
        }

        var myVideoList = res.data.data.rows;
        var newVideoList = me.data.myVideoList;

        me.setData({
          myVideoList: newVideoList.concat(myVideoList),
          myVideoPage: page,
          myVideoTotal: res.data.data.total
        });
        // console.log(me.data.myVideoList);
        // console.log(me.data.myVideoPage);
        // console.log(me.data.myVideoTotal);
      }
    })

  },

  getMyLikesList: function (page){

    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中...',
    });

    wx.request({
      url: serverUrl + '/video/showMyLike?page=' + page + '&userId=' + me.data.userId,
      method: "POST",
      success: function (res) {
        wx.hideLoading();
        console.log(res.data);
        // 判断当前页page是否是第一页，如果是第一页，那么设置videoList为空 (为了下拉刷新设置)
        if (page === 1) {
          me.setData({
            likeVideoList: []
          });
        }
    
        var likeVideoList = res.data.data.rows;
        var newVideoList = me.data.likeVideoList;

        me.setData({
          likeVideoList: newVideoList.concat(likeVideoList),
          likeVideoPage: page,
          likeVideoTotal: res.data.data.total
        });

        // console.log(me.data.likeVideoList);
        // console.log(me.data.likeVideoPage);
        // console.log(me.data.likeVideoTotal);

      }
    })

  },

  getMyFollowList: function (page) {

    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中...',
    });

    wx.request({
      url: serverUrl + '/video/showMyFollow?page=' + page + '&userId=' + me.data.userId,
      method: "POST",
      success: function (res) {
        wx.hideLoading();
        console.log(res.data);
        // 判断当前页page是否是第一页，如果是第一页，那么设置videoList为空 (为了下拉刷新设置)
        if (page === 1) {
          me.setData({
            followVideoList: []
          });
        }
    
        var followVideoList = res.data.data.rows;
        var newVideoList = me.data.followVideoList;

        me.setData({
          followVideoList: newVideoList.concat(followVideoList),
          followVideoPage: page,
          followVideoTotal: res.data.data.total
        });

        // console.log(me.data.followVideoList);
        // console.log(me.data.followVideoPage);
        // console.log(me.data.followVideoTotal);
 
      }
    })

  },


  // onPullDownRefresh: function() {
  //   debugger;
  //   var me = this;
  //   wx.showNavigationBarLoading();
  //   if (!myWorkFalg){
  //     me.getMyVideoList(1);
  //   } else if (!myLikesFalg){
  //     me.getMyLikesList(1);
  //   } else if (!myFollowFalg) {
  //     me.getMyFollowList(1);
  //   }
  // },


  onReachBottom:function() {
    var me = this;
    var myWorkFalg = me.data.myWorkFalg;
    var myLikesFalg = me.data.myLikesFalg;
    var myFollowFalg = me.data.myFollowFalg;
    if (!myWorkFalg){
      var curPage = me.data.myVideoPage;
      var totalPage = me.data.myVideoTotal;
      // 所有视频Page已用完
      if (curPage==totalPage){
        wx.showToast({
          title: '已经没有视频啦~~',
          icon: 'none'
        })
        return;
      }
      // 视频Page还未用完
      var page = curPage + 1;
      me.getMyVideoList(page);
    } else if (!myLikesFalg){
      var curPage = me.data.likeVideoPage;
      var totalPage = me.data.likeVideoTotal;
      // 所有视频Page已用完
      if (curPage==totalPage){
        wx.showToast({
          title: '已经没有视频啦~~',
          icon: 'none'
        })
        return;
      }
      // 视频Page还未用完
      var page = curPage + 1;
      me.getMyLikesList(page);
    } else if (!myFollowFalg) {
      var curPage = me.data.followVideoPage;
      var totalPage = me.data.followVideoTotal;
      // 所有视频Page已用完
      if (curPage==totalPage){
        wx.showToast({
          title: '已经没有视频啦~~',
          icon: 'none'
        })
        return;
      }
      // 视频Page还未用完
      var page = curPage + 1;
      me.getMyFollowList(page);
    }
  
  },

  
  doSelectWork: function () {
    this.setData({
      isSelectedWork: "video-info-selected",
      isSelectedLike: "",
      isSelectedFollow: "",

      // 所有恢复成默认值
      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,

      //切换tab
      myWorkFalg: false,
      myLikesFalg: true,
      myFollowFalg: true
    });

    this.getMyVideoList(1);
  },

  doSelectLike: function () {

    this.setData({
      isSelectedWork: "",
      isSelectedLike: "video-info-selected",
      isSelectedFollow: "",

      // 所有恢复成默认值
      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,

      //切换tab
      myWorkFalg: true,
      myLikesFalg: false,
      myFollowFalg: true

    });

    this.getMyLikesList(1);

  },

  doSelectFollow: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedLike: "",
      isSelectedFollow: "video-info-selected",

      // 所有恢复成默认值
      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,

      //切换tab
      myWorkFalg: true,
      myLikesFalg: true,
      myFollowFalg: false
    });

    this.getMyFollowList(1);

  },

  showVideo: function(e){
    var me = this;
    console.log(e);
    var arrindex = e.currentTarget.dataset.arrindex;
    if (!me.data.myWorkFalg){
      var videoInfo = me.data.myVideoList[arrindex];
    } else if (!me.data.myLikesFalg){
      var videoInfo = me.data.likeVideoList[arrindex];
    } else if (!me.data.myFollowFalg) {
      var videoInfo = me.data.followVideoList[arrindex];
    }
    // Json对象无法通过跳转传到下一个页面，先转成string
    var videoInfo = JSON.stringify(videoInfo);
    wx.redirectTo({
      url: '../videoinfo/videoinfo?videoInfo='+videoInfo,
    })


  }


})
