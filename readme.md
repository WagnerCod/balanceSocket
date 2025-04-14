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

## Exemplo de script no ionic angular ja formatado para tonelada, kg, e gramas

exemploConexaoBalanca() {
  const host = '00.00.00.00';
  const port = 0000;

  if (!(window as any).cordova.plugins?.balancaSocket) {
    console.error('Plugin balancaSocket não encontrado');
    this.peso = 'Erro: Plugin não carregado';
    return;
  }

  cordova.plugins.balancaSocket.conectarBalanca(
    host,
    port,
    (pesoRaw: string) => {
      const formatarPeso = (raw: string): string => {
        const cleanRaw = raw.trim();

        // Ignora os dois primeiros caracteres e espaço, se houver
        let startIndex = 2;
        if (cleanRaw[startIndex] === ' ') {
          startIndex += 1;
        }

        const pesoSlice = cleanRaw.slice(startIndex, startIndex + 7);
        if (!/^\d{7}$/.test(pesoSlice)) return 'Peso inválido';

        const pesoGramas = parseInt(pesoSlice, 10);
        const toneladas = Math.floor(pesoGramas / 1000000);
        const restoGramas = pesoGramas % 1000000;
        const quilogramas = Math.floor(restoGramas / 1000);
        const gramas = restoGramas % 1000;

        let resultado = '';
        if (toneladas > 0) resultado += `${toneladas}t `;
        if (quilogramas > 0 || toneladas > 0) resultado += `${quilogramas}kg `;
        resultado += `${gramas}g`;
        return resultado.trim();
      };

      this.peso = formatarPeso(pesoRaw);
    },
    (err: any) => {
      console.error('Erro ao conectar:', err);
      this.peso = 'Erro na balança';
    }
  );
}
