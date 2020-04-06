
function uploadVideo() {
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
      if (duration > 30) {
        wx.showToast({
          title: '视频长度不能超过30秒',
          icon: "none",
          duration: 2500
        })
      } else if (duration < 1) {
        wx.showToast({
          title: '视频长度不能小于1秒',
          icon: "none",
          duration: 2500
        })
      } else {
        // 跳转到选择bgm的页面
        wx.navigateTo({
          url: '../chooseBgm/chooseBgm?' +
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

//videoUtil
module.exports={
  uploadVideo: uploadVideo
}



