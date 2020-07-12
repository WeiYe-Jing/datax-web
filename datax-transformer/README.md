# transformer安装步骤
## 1.md5函数安装
### 1.在datax目录下面创建local_storage/transformer目录
### 2.如果需要用md5函数，那么进入local_storage/transformer目录，在下面创建md5目录，解压datax-transformer_2.1.2_1.tar.gz，将lib目录的所有jar包放入此目录中，接着创建transformer.json文件
内容为：
{
"name": "md5",
"class": "com.wugui.datax.transformer.Md5Transformer"
}


## 2.replaceNewLineSymbol函数安装
### 1.在datax目录下面创建local_storage/transformer目录
### 2.如果需要用md5函数，那么进入local_storage/transformer目录，在下面创建replaceNewLineSymbol目录，解压datax-transformer_2.1.2_1.tar.gz，将lib目录的所有jar包放入此目录中，接着创建transformer.json文件
内容为：
{
"name": "replaceNewLineSymbol",
"class": "com.wugui.datax.transformer.ReplaceNewLineSymbolTransformer"
}


