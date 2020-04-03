// var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
  },
  onLoad: function (params) {
      var me = this;
      var user = app.userInfo;
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '请等待...',
      });
      wx.request({
        url: serverUrl +'/user/query?userId=' + user.id,
        method: "POST",
        header: {
          'content-type': 'application/json', // 默认值
        },
        success : function (res) { 
          console.log(res.data); 
          wx.hideLoading();
          if (res.data.status == 200){
            var userInfo = res.data.data;
            var faceurl = "../resource/images/noneface.png";
            if (userInfo.faceImage!=null && userInfo.faceImage!='' && userInfo.faceImage!=undefined){
              faceurl = serverUrl + userInfo.faceImage; 
            }
            me.setData({
              faceUrl: faceurl,
              fansCounts: userInfo.fansCounts,
              followCounts: userInfo.followCounts,
              receiveLikeCounts: userInfo.receiveLikeCounts,
              nickname: userInfo.nickname
            })
          } 
        }
      })
  },

  followMe: function (e) {

  },

  logout: function () {
    var user = app.userInfo;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    wx.request({
      url: serverUrl +'/logout?userId='+user.id,
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
      },
      success : function (res) {  
        console.log(res.data); 
        wx.hideLoading();
        if (res.data.status == 200){
          wx.showToast({
            title: "用户注销成功",
            icon:'none',
            duration:2000
          }),
          app.userInfo = null;
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
      success (res) {
        // tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths;
        console.log(tempFilePaths);
        var user = app.userInfo;
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
          },
          success : function (res){
            var data = JSON.parse(res.data);
            wx.hideLoading();
            console.log(data);
            if (data.status == 200){
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
            }
            //do something
          }
        })
      }
    })
  },

  uploadVideo: function(){
    var me = this;
    wx.chooseVideo({
      sourceType: ['album'],
      success(res) {
        console.log(res)
        // duration: 45.8
        // errMsg: "chooseVideo:ok"
        // height: 960
        // size: 7052793
        // tempFilePath: "http://tmp/wxc6bc584dcd71d0c0.o6zAJs_HWjRdvdjyPi8ofW1bUZ9I.NEgYZpOPuirl18c4fc690f9116e803b314f4b873546d.MP4"
        // thumbTempFilePath: "http://tmp/wxc6bc584dcd71d0c0.o6zAJs_HWjRdvdjyPi8ofW1bUZ9I.2bzbhI4pjCzkc025a64581b45b7ec39f25ea452f4927.jpg"
        // width: 544
        var duration = res.duration;
        var tmpHeight = res.height;
        var tmpWidth = res.width;
        var tmpVideoUrl = res.tempFilePath;
        var tmpCoverUrl = res.thumbTempFilePath;

        if (duration>30){
          wx.showToast({
            title: '视频长度不能超过30秒',
            icon: "none",
            duration: 2500
          })
        } else if (duration<1){
          wx.showToast({
            title: '视频长度不能小于1秒',
            icon: "none",
            duration: 2500
          }) 
        } else {
          // 跳转到选择bgm的页面
          wx.navigateTo({
            url: '../chooseBgm/chooseBgm?'+
            'duration=' + duration +
            '&tmpHeight=' + tmpHeight +
            '&tmpWidth=' + tmpWidth +
            '&tmpVideoUrl=' + tmpVideoUrl +
            '&tmpCoverUrl=' + tmpCoverUrl
          })
            
        }

      }
    })
  }


})
