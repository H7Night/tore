[English](README.md)

# Tore

Tore 是一款记录力量训练和有氧训练的日志应用。

名字取自日语「筋トレ」，即「筋肉トレーニング」的简称，意为力量训练。Tore 帮助你建立稳定的训练节奏，记录每次训练，并观察长期训练是否均衡。

## 功能

- 记录力量训练和有氧训练
- **训练日历**：按日期查看训练记录，并打开对应训练详情
- **动作进度**：查看最佳组、估算 1RM 和长期训练总量
- 创建包含训练日、休息日和自定义日标题的训练计划
- **灵活编辑训练**：一次添加多个动作，也可添加计划外动作
- 以 JSON 格式导入和导出训练计划
- 按身体部位、肌肉群和器械整理、筛选训练动作
- **每周摘要**：在主页查看本周训练和当天计划动作
- 通过热力图、趋势、平衡度和计划执行率查看训练统计
- 记录体重并查看长期变化
- **Health Connect 同步**：可选同步已完成的训练
- **主题配色**：在浅色和深色主题之外选择配色方案
- 使用支持暂停和继续的训练计时器
- 删除数据前明确确认
- 备份完整数据库，或按范围导出训练数据
- 支持英文和简体中文

## 技术栈

- Kotlin2.2.x
- Jetpack Compose 和 Material 3 Expressive
- Hilt 依赖注入
- Room 与 KSP 本地持久化
- WorkManager 定时备份
- 使用可序列化路由的 Navigation Compose

## 构建

```bash
# 构建debug APK
./gradlew assembleDebug

# 在已连接的设备或模拟器上构建并安装
./gradlew installDebug

# 运行单元测试
./gradlew test

# 在已连接的设备或模拟器上运行仪器化测试
./gradlew connectedAndroidTest
```

`scripts/`目录提供了适用于 Windows 和 Unix 的常用构建命令脚本，所有脚本均为非交互式。

## 发布

GitHub Actions 负责准备发布：更新版本号、更新日志和 Fastlane 元数据，然后创建 `release/<version>` 拉取请求。该 PR 合并到 `main` 后，工作流会构建并签名 APK 和 AAB，再创建附带这两个文件的 GitHub Release 草稿。维护者会在 GitHub Releases 中发布草稿。本项目不会上传到 Google Play。

## 许可证

Tore 使用 GNU 通用公共许可证第 3 版或更高版本发布。

完整许可证文本请参阅[LICENSE](LICENSE)。

## 更新日志

- [完整更新日志](CHANGELOG.md)
- [未发布改动](CHANGELOG.md#unreleased)
