(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6b3b7c1a"],{"2b10":function(e,t,n){"use strict";n.d(t,"e",(function(){return i})),n.d(t,"j",(function(){return a})),n.d(t,"h",(function(){return l})),n.d(t,"i",(function(){return r})),n.d(t,"c",(function(){return u})),n.d(t,"l",(function(){return s})),n.d(t,"b",(function(){return c})),n.d(t,"g",(function(){return d})),n.d(t,"f",(function(){return p})),n.d(t,"d",(function(){return f})),n.d(t,"a",(function(){return g})),n.d(t,"k",(function(){return b}));var o=n("b775");function i(e){return Object(o["a"])({url:"api/job/pageList",method:"get",params:e})}function a(e){return Object(o["a"])({url:"/api/job/trigger",method:"post",data:e})}function l(e){return Object(o["a"])({url:"/api/job/start?id="+e,method:"post"})}function r(e){return Object(o["a"])({url:"/api/job/stop?id="+e,method:"post"})}function u(){return Object(o["a"])({url:"api/jobGroup/list",method:"get"})}function s(e){return Object(o["a"])({url:"/api/job/update",method:"post",data:e})}function c(e){return Object(o["a"])({url:"/api/job/add/",method:"post",data:e})}function d(e){return Object(o["a"])({url:"/api/job/remove/"+e,method:"post"})}function p(e){return Object(o["a"])({url:"/api/job/nextTriggerTime?cron="+e,method:"get"})}function f(e){return Object(o["a"])({url:"api/job/list",method:"get",params:e})}function g(e){return Object(o["a"])({url:"/api/job/batchAdd",method:"post",data:e})}function b(e){return Object(o["a"])({url:"/api/job/triggerList",method:"post",data:e})}},"32e8":function(e,t,n){"use strict";n.d(t,"b",(function(){return i})),n.d(t,"a",(function(){return a})),n.d(t,"c",(function(){return l})),n.d(t,"d",(function(){return r}));var o=n("b775");function i(e){return Object(o["a"])({url:"api/log/pageList",method:"get",params:e})}function a(e,t,n){return Object(o["a"])({url:"/api/log/clearLog?jobGroup="+e+"&jobId="+t+"&type="+n,method:"post"})}function l(e){return Object(o["a"])({url:"/api/log/killJob",method:"post",data:e})}function r(e,t,n,i){return Object(o["a"])({url:"/api/log/logDetailCat?executorAddress="+e+"&triggerTime="+t+"&logId="+n+"&fromLineNum="+i,method:"get"})}},"333d":function(e,t,n){"use strict";var o=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"pagination-container",class:{hidden:e.hidden}},[n("el-pagination",e._b({attrs:{background:e.background,"current-page":e.currentPage,"page-size":e.pageSize,layout:e.layout,"page-sizes":e.pageSizes,total:e.total},on:{"update:currentPage":function(t){e.currentPage=t},"update:current-page":function(t){e.currentPage=t},"update:pageSize":function(t){e.pageSize=t},"update:page-size":function(t){e.pageSize=t},"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}},"el-pagination",e.$attrs,!1))],1)},i=[];n("c5f6");Math.easeInOutQuad=function(e,t,n,o){return e/=o/2,e<1?n/2*e*e+t:(e--,-n/2*(e*(e-2)-1)+t)};var a=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)}}();function l(e){document.documentElement.scrollTop=e,document.body.parentNode.scrollTop=e,document.body.scrollTop=e}function r(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function u(e,t,n){var o=r(),i=e-o,u=20,s=0;t="undefined"===typeof t?500:t;var c=function e(){s+=u;var r=Math.easeInOutQuad(s,o,i,t);l(r),s<t?a(e):n&&"function"===typeof n&&n()};c()}var s={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(e){this.$emit("update:page",e)}},pageSize:{get:function(){return this.limit},set:function(e){this.$emit("update:limit",e)}}},methods:{handleSizeChange:function(e){this.$emit("pagination",{page:this.currentPage,limit:e}),this.autoScroll&&u(0,800)},handleCurrentChange:function(e){this.$emit("pagination",{page:e,limit:this.pageSize}),this.autoScroll&&u(0,800)}}},c=s,d=(n("e498"),n("2877")),p=Object(d["a"])(c,o,i,!1,null,"6af373ef",null);t["a"]=p.exports},"51c4":function(e,t,n){},6724:function(e,t,n){"use strict";n("8d41");var o="@@wavesContext";function i(e,t){function n(n){var o=Object.assign({},t.value),i=Object.assign({ele:e,type:"hit",color:"rgba(0, 0, 0, 0.15)"},o),a=i.ele;if(a){a.style.position="relative",a.style.overflow="hidden";var l=a.getBoundingClientRect(),r=a.querySelector(".waves-ripple");switch(r?r.className="waves-ripple":(r=document.createElement("span"),r.className="waves-ripple",r.style.height=r.style.width=Math.max(l.width,l.height)+"px",a.appendChild(r)),i.type){case"center":r.style.top=l.height/2-r.offsetHeight/2+"px",r.style.left=l.width/2-r.offsetWidth/2+"px";break;default:r.style.top=(n.pageY-l.top-r.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",r.style.left=(n.pageX-l.left-r.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return r.style.backgroundColor=i.color,r.className="waves-ripple z-active",!1}}return e[o]?e[o].removeHandle=n:e[o]={removeHandle:n},n}var a={bind:function(e,t){e.addEventListener("click",i(e,t),!1)},update:function(e,t){e.removeEventListener("click",e[o].removeHandle,!1),e.addEventListener("click",i(e,t),!1)},unbind:function(e){e.removeEventListener("click",e[o].removeHandle,!1),e[o]=null,delete e[o]}},l=function(e){e.directive("waves",a)};window.Vue&&(window.waves=a,Vue.use(l)),a.install=l;t["a"]=a},7456:function(e,t,n){},"8ba2":function(e,t,n){"use strict";n.r(t);var o=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"全部"},model:{value:e.listQuery.jobId,callback:function(t){e.$set(e.listQuery,"jobId",t)},expression:"listQuery.jobId"}}),e._v(" "),n("el-select",{attrs:{placeholder:"执行器"},model:{value:e.listQuery.jobGroup,callback:function(t){e.$set(e.listQuery,"jobGroup",t)},expression:"listQuery.jobGroup"}},e._l(e.executorList,(function(e){return n("el-option",{key:e.id,attrs:{label:e.title,value:e.id}})})),1),e._v(" "),n("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"类型"},model:{value:e.listQuery.logStatus,callback:function(t){e.$set(e.listQuery,"logStatus",t)},expression:"listQuery.logStatus"}},e._l(e.logStatusList,(function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1),e._v(" "),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.fetchData}},[e._v("\n      搜索\n    ")]),e._v(" "),n("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"el-icon-edit"},on:{click:e.handlerDelete}},[e._v("\n      清除\n    ")])],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"任务ID",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.jobId))]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"任务描述"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.jobDesc))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"调度时间",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.triggerTime))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"调度结果",align:"center",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{style:"color:"+(500==t.row.triggerCode?"red":"")},[e._v(e._s(e.statusList.find((function(e){return e.value===t.row.triggerCode})).label))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"调度备注",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-popover",{attrs:{placement:"bottom",width:"400",trigger:"click"}},[n("h5",{domProps:{innerHTML:e._s(t.row.triggerMsg)}}),e._v(" "),n("el-button",{attrs:{slot:"reference"},slot:"reference"},[e._v("查看")])],1)]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"执行时间",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.handleTime))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"执行结果",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{style:"color:"+(500==t.row.handleCode?"red":"")},[e._v(e._s(e.statusList.find((function(e){return e.value===t.row.handleCode})).label))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"执行备注",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-popover",{attrs:{placement:"bottom",width:"400",trigger:"click"}},[n("h5",{domProps:{innerHTML:e._s(t.row.handleMsg)}}),e._v(" "),n("el-button",{attrs:{slot:"reference"},slot:"reference"},[e._v("查看")])],1)]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"操作",align:"center",width:"300"},scopedSlots:e._u([{key:"default",fn:function(t){var o=t.row;return[n("el-button",{directives:[{name:"show",rawName:"v-show",value:o.executorAddress,expression:"row.executorAddress"}],attrs:{type:"primary"},on:{click:function(t){return e.handleViewJobLog(o)}}},[e._v("日志查看")]),e._v(" "),n("el-button",{directives:[{name:"show",rawName:"v-show",value:0===o.handleCode&&200===o.triggerCode,expression:"row.handleCode===0 && row.triggerCode===200"}],attrs:{type:"primary"},on:{click:function(t){return e.killRunningJob(o)}}},[e._v("\n          终止任务\n        ")])]}}])})],1),e._v(" "),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.listQuery.current,limit:e.listQuery.size},on:{"update:page":function(t){return e.$set(e.listQuery,"current",t)},"update:limit":function(t){return e.$set(e.listQuery,"size",t)},pagination:e.fetchData}}),e._v(" "),n("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible,width:"600px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",attrs:{rules:e.rules,model:e.temp,"label-position":"center","label-width":"100px"}},[n("el-row",[n("el-col",{attrs:{span:14,offset:5}},[n("el-form-item",{attrs:{label:"执行器"}},[n("el-input",{attrs:{size:"medium",value:"全部",disabled:!0}})],1)],1)],1),e._v(" "),n("el-row",[n("el-col",{attrs:{span:14,offset:5}},[n("el-form-item",{attrs:{label:"任务"}},[n("el-input",{attrs:{size:"medium",value:"全部",disabled:!0}})],1)],1)],1),e._v(" "),n("el-row",[n("el-col",{attrs:{span:14,offset:5}},[n("el-form-item",{attrs:{label:"执行器"}},[n("el-select",{staticStyle:{width:"230px"},attrs:{placeholder:"请选择执行器"},model:{value:e.temp.deleteType,callback:function(t){e.$set(e.temp,"deleteType",t)},expression:"temp.deleteType"}},e._l(e.deleteTypeList,(function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1)],1)],1)],1),e._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("\n        取消\n      ")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:e.deleteLog}},[e._v("\n        确定\n      ")])],1)],1),e._v(" "),n("el-dialog",{attrs:{title:"日志查看",visible:e.dialogVisible,width:"95%"},on:{"update:visible":function(t){e.dialogVisible=t}}},[n("div",{staticClass:"log-container"},[n("pre",{attrs:{loading:e.logLoading},domProps:{textContent:e._s(e.logContent)}})]),e._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("\n        关闭\n      ")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:e.loadLog}},[e._v("\n        刷新日志\n      ")])],1)])],1)},i=[],a=n("32e8"),l=n("2b10"),r=n("6724"),u=n("333d"),s={name:"JobLog",components:{Pagination:u["a"]},directives:{waves:r["a"]},filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]}},data:function(){return{dialogVisible:!1,list:null,listLoading:!0,total:0,listQuery:{current:1,size:10,jobGroup:0,jobId:"",logStatus:-1,filterTime:""},dialogPluginVisible:!1,pluginData:[],dialogFormVisible:!1,dialogStatus:"",executorList:"",textMap:{create:"Clear"},rules:{},temp:{deleteType:1,jobGroup:0,jobId:0},statusList:[{value:500,label:"失败"},{value:502,label:"失败(超时)"},{value:200,label:"成功"},{value:0,label:"无"}],deleteTypeList:[{value:1,label:"清理一个月之前日志数据"},{value:2,label:"清理三个月之前日志数据"},{value:3,label:"清理六个月之前日志数据"},{value:4,label:"清理一年之前日志数据"},{value:5,label:"清理一千条以前日志数据"},{value:6,label:"清理一万条以前日志数据"},{value:7,label:"清理三万条以前日志数据"},{value:8,label:"清理十万条以前日志数据"},{value:9,label:"清理所有日志数据"}],logStatusList:[{value:-1,label:"全部"},{value:1,label:"成功"},{value:2,label:"失败"},{value:3,label:"进行中"}],jobLogQuery:{executorAddress:"",triggerTime:"",id:"",fromLineNum:1},logContent:"",logShow:!1,logLoading:!1}},created:function(){this.fetchData(),this.getExecutor()},methods:{fetchData:function(){var e=this;this.listLoading=!0;var t=Object.assign({},this.listQuery),n=this.$route.query.jobId;n>0&&!t.jobId?t.jobId=n:n||t.jobId||(t.jobId=0),a["b"](t).then((function(t){var n=t.content;e.total=n.recordsTotal,e.list=n.data,e.listLoading=!1}))},getExecutor:function(){var e=this;l["c"]().then((function(t){var n=t.content;e.executorList=n;var o={id:0,title:"全部"};e.executorList.unshift(o),e.listQuery.jobGroup=e.executorList[0].id}))},handlerDelete:function(){var e=this;this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},deleteLog:function(){var e=this;a["a"](this.temp.jobGroup,this.temp.jobId,this.temp.deleteType).then((function(t){e.fetchData(),e.dialogFormVisible=!1,e.$notify({title:"Success",message:"Delete Successfully",type:"success",duration:2e3})}))},handleViewJobLog:function(e){this.dialogVisible=!0,this.jobLogQuery.executorAddress=e.executorAddress,this.jobLogQuery.id=e.id,this.jobLogQuery.triggerTime=Date.parse(e.triggerTime),!1===this.logShow&&(this.logShow=!0),this.loadLog()},loadLog:function(){var e=this;this.logLoading=!0,a["d"](this.jobLogQuery.executorAddress,this.jobLogQuery.triggerTime,this.jobLogQuery.id,this.jobLogQuery.fromLineNum).then((function(t){"\n"===t.content.logContent||(e.logContent=t.content.logContent),e.logLoading=!1}))},killRunningJob:function(e){var t=this;a["c"](e).then((function(e){t.fetchData(),t.dialogFormVisible=!1,t.$notify({title:"Success",message:"Kill Successfully",type:"success",duration:2e3})}))}}},c=s,d=(n("c3e3"),n("2877")),p=Object(d["a"])(c,o,i,!1,null,"25f54d24",null);t["default"]=p.exports},"8d41":function(e,t,n){},c3e3:function(e,t,n){"use strict";n("51c4")},e498:function(e,t,n){"use strict";n("7456")}}]);