var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
  },
  onLoad: function (params) {
    var me = this;
    // var user = app.userInfo;
    // 修改原有的全局对象为本地缓存
    var user = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    wx.request({
      url: serverUrl + '/user/query?userId=' + user.id,
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
            nickname: userInfo.nickname
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
    })
  },

  followMe: function (e) {

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
  }


})
