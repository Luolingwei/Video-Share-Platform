const app = getApp()

Page({
    data: {
        reasonType: "请选择原因",
        reportReasonArray: app.reportReasonArray,
        publishUserId:"",
        videoId: "",
        title: ""
    },

    onLoad:function(params) {
      var me = this;
      console.log(params);
      me.setData({
        publishUserId: params.publisherId,
        videoId: params.videoId,
      })

    },

    changeMe:function(e) {

      var me = this;
      var index = e.detail.value;
      var title = me.data.reportReasonArray[index];
      me.setData({
        title: title
      })

    },

    submitReport:function(e) {
      var me = this;
      var user = app.getGlobalUserInfo();
      var dealUserId = me.data.publishUserId;
      var dealVideoId = me.data.videoId;
      var title = me.data.title;
      var content = e.detail.value.reasonContent;
      var userid = user.id;

      if (title == null || title == '' || title == undefined){
        wx.showToast({
          title: '选择举报理由',
          icon: 'none'
        })
        return;
      }

      var serverUrl = app.serverUrl;
      wx.request({
        url: serverUrl + '/user/reportUser',
        method: "POST",
        data: {
          dealUserId: dealUserId,
          dealVideoId: dealVideoId,
          title: title,
          content: content,
          userid: userid
        },
        header: {
          'content-type': 'application/json', // 默认值
          'userId': user.id,
          'userToken': user.userToken
        },
        success: function(res){
          console.log(res.data);
          wx.showToast({
            title: res.data.data,
            duration: 2000,
            icon: 'none',
            success: function(){
              wx.navigateBack({
                delta: 1,
              })
            }
          })
        }
      })

    }
    
})
