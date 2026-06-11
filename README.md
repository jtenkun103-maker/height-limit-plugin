# HeightLimitPlugin

Spigot/Paper 向けに建築可能な高度の上限を1000に拡張するプラグインです。

## 動作環境
- Paper / Spigot 1.21.x
- Java 21+

## ビルド方法

### 前提条件
- JDK 21 以上
- Maven 3.6 以上

### ビルド手順
```bash
cd height-limit-plugin
mvn package
```
`target/HeightLimitPlugin-1.0.0.jar` が生成されます。

## インストール
1. ビルドした `HeightLimitPlugin-1.0.0.jar` をサーバーの `plugins/` フォルダに入れる
2. サーバーを再起動（または `/reload confirm`）

## コマンド
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/heightlimit` | 現在の高度設定と自分の高度を表示 | `heightlimit.info`（デフォルト: 全員） |
| `/heightlimit reload` | config.yml をリロード | `heightlimit.bypass`（デフォルト: OP） |

## 権限
| 権限 | 説明 | デフォルト |
|-----|------|---------|
| `heightlimit.info` | 情報表示コマンドを使用 | 全員 |
| `heightlimit.bypass` | 高度制限をバイパス（管理者用） | OPのみ |

## config.yml 設定項目
```yaml
max-build-height: 1000     # 最大建築Y座標
creative-only: false       # trueにするとクリエイティブモードのみ制限緩和
show-warning-message: true # 制限を超えた際にメッセージを表示
warning-message: "§c高度 %height% はサーバーの制限（1000）を超えています！"
```

## ⚠️ 重要な注意点

Minecraft Java版のワールド高度はサーバー設定（`bukkit.yml` や ワールドの `level.dat`）で決まります。
プラグインだけではワールド自体の上限（通常Y=320）は変更できません。

**高度1000まで建築可能にするには：**

1. 新規ワールドを作成する前に `bukkit.yml` でカスタムジェネレータを使うか、
2. Paper の `paper-world-defaults.yml` で `max-world-height` を設定する必要があります。

または **Multiverse-Core** + **Multiverse-NetherPortals** などと組み合わせて
カスタム高度のワールドを作成することをお勧めします。

このプラグインは「設定した上限を超えた場所でのブロック設置を防ぐ」機能を提供します。
