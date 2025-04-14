# cordova.plugin.balancaSocket

Plugin Cordova personalizado para comunicação com balanças via TCP/IP, utilizado para leitura de peso bruto e conversão para unidades padrão (toneladas, quilos e gramas).

## 📦 Instalação

Clone ou copie o plugin para dentro do seu projeto Ionic/Cordova:

```bash
cordova plugin add ./caminho/para/cordova-plugin-balanca-socket --link

## Estrutura do Plugin
cordova.plugin.balancaSocket/
│
├── plugin.xml
├── www/
│   └── balancaSocket.js
└── src/
    └── android/
        └── BalancaSocket.java

## Exemplo de script no 