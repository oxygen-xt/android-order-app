# Android 点单 App

使用 Kotlin 和 Jetpack Compose 开发的课程设计项目。

## 功能

- 浏览 6 道菜品的名称、简介和价格
- 使用加减按钮调整数量，数量不能小于 0
- 实时显示总份数与合计金额
- 在确认弹窗中核对菜品明细
- 模拟提交订单，显示成功提示并清空当前选择

## 运行

1. 使用 Android Studio 打开仓库根目录。
2. 根据项目提示安装 SDK 并等待 Gradle 同步完成。
3. 选择 Android 模拟器或真机，运行 `app`。

项目当前配置：`minSdk = 24`，`compileSdk = 37`，`targetSdk = 37`。首次运行需要联网下载构建依赖。本机 SDK 路径由 Android Studio 在 `local.properties` 中生成。

## 关键代码

`app/src/main/java/com/example/myapplication/MainActivity.kt`

- `Dish` 和 `menu`：菜品数据与固定菜单
- `OrderScreen`：点单状态、金额计算和订单弹窗
- `DishCard`：菜品卡片和数量操作

## 示例

6 道菜各选 1 份：26 + 24 + 16 + 38 + 14 + 2 = 120 元。

订单在内存中保存，提交为本地模拟流程；未接入后台服务或真实支付。
