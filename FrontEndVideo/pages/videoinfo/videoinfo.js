// var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    cover: "cover",
  },

  videoCtx: {},

  onLoad: function (params) {    
  },

  onShow: function () {
  },

  onHide: function () {
  },

  showSearch: function () {
    
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })

  },

  showPublisher: function () {
  },


  upload: function () {
  },

  showIndex: function () {
  },

  showMine: function () {
  },

  likeVideoOrNot: function () {
  },

  shareMe: function() {
  },

  onShareAppMessage: function (res) {
  },


  leaveComment: function() {
  },

  replyFocus: function(e) {
  },

  saveComment:function(e) {
  },


  getCommentsList: function(page) {
  },

  onReachBottom: function() {
  }
})