const app = getApp()

Page({
    data: {
      bgmList: [],
      serverUrl: "",
      videoParams: {},
    },

    onLoad: function (params) {
      console.log(params);
      var me = this;
      me.setData({
        videoParams: params
      })
      wx.showLoading({
        title: '请等待...',
      });
      var serverUrl = app.serverUrl;
      // 调用后端
      wx.request({
        url: serverUrl + '/bgm/list',
        method: "POST",
        header: {
          'content-type': 'application/json' // 默认值
        },
        success: function (res) {
          console.log(res.data);
          wx.hideLoading();
          var bgmList = res.data.data;
          me.setData({
            bgmList: bgmList,
            serverUrl: serverUrl,
          })
        }
      })
    },

    upload: function(e) {
      var me = this;
      console.log(e.detail.value);
      var bgmId = e.detail.value.bgmId;
      var desc = e.detail.value.desc;
      var duration = me.data.videoParams.duration;
      var tmpHeight = me.data.videoParams.tmpHeight;
      var tmpWidth = me.data.videoParams.tmpWidth;
      var tmpVideoUrl = me.data.videoParams.tmpVideoUrl;
      var tmpCoverUrl = me.data.videoParams.tmpCoverUrl;

      var user = app.userInfo;
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '上传中',
      });
      wx.uploadFile({
        url: serverUrl + '/video/upload',
        formData: {
          userId: user.id,
          bgmId: bgmId,
          desc: desc,
          videoSeconds: duration,
          videoHeight: tmpHeight,
          videoWidth: tmpWidth
        },
        filePath: tmpVideoUrl,
        name: 'file',
        header: {
          'content-type': 'application/json', // 默认值
        },
        success : function (res){
          wx.hideLoading();
          var data = JSON.parse(res.data);
          console.log(data);
          if (data.status == 200){
            wx.showToast({
              title: '上传成功',
              icon: "success"
            }),
            wx.navigateBack({
              delta: 1,
            })
            // 视频上传成功后, 上传封面
          //   var videoId = data.data;
          //   wx.showLoading({
          //     title: '上传中',
          //   });
          //   wx.uploadFile({
          //     url: serverUrl + '/video/uploadCover',
          //     formData: {
          //       userId: user.id,
          //       videoId: videoId
          //     },
          //     filePath: tmpCoverUrl,
          //     name: 'file',
          //     header: {
          //       'content-type': 'application/json', // 默认值
          //     },
          //     success : function (res){
          //       var data = JSON.parse(res.data);
          //       if (data.status == 200){
          //         wx.showToast({
          //           title: '上传成功',
          //           icon: "success"
          //         })
          //         wx.navigateBack({
          //           delta: 1,
          //         })
          //       } else {
          //         wx.showToast({
          //           title: '上传失败',
          //         })
          //     }
          //   }
          // })
        } else {
            wx.showToast({
              title: '上传失败',
            })
          }
        }
      })
    }
})

