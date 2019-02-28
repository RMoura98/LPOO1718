## LPOO1718_T3G7 -  Entrega Projeto Final - MIEIC 2017-2018

## Setup
### Instruções de Setup do Projeto.
O projeto foi gerado pelo utilitário de Setup do LibGDX e como tal é um projeto com integração Gradle.

O ambiente que usamos para o desenvolver foi o Android Studio e como tal é este IDE que recomendamos. 

No entanto não prevemos qualquer obstáculo em utilizar IntelliJ ou Eclipse para o modificar.

### Instruções para correr o jogo.
Os executáveis do jogo (tanto para PC como para Android) podem ser gerados pelo Android Studio uma vez que os ficheiros de configuração do Gradle detém as instruções de como efetuar os builds. 

Também é possível gerá-los através da linha de comandos - https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline
Para questões de simplicidade seguem neste repositório os executáveis (.jar e .apk) já gerados.

Para executar o jogo em computador é necessário ter previamente instalado o JRE - [Java Runtime Environment](https://java.com/download/) e os assets do jogo têm que estar na mesma pasta do ficheiro .jar (estes podem ser encontrados na pasta android/assets).

Para executar o jogo em android apenas é necessário instalar o .apk e corrê-lo.

## Documentação

### Javadocs
Estão presentes no repositório.

### [Diagrama UML de Classes (simplificado)](https://i.imgur.com/Md7Y6BQ.png)
Aqui segue um diagrama de classes do nosso projeto que não apresenta os campos nem métodos públicos das classes, apenas as relações estabelecidas entre elas.
![Diagrama](https://i.imgur.com/Md7Y6BQ.png)

### [Diagrama UML de Classes (extenso)](https://i.imgur.com/FXwCqdv.png)
Aqui segue um diagrama de classes gerado pelo IntelliJ que mostra a API pública de cada classe.
![enter image description here](https://i.imgur.com/FXwCqdv.png)

## Principais obstáculos encontrados.
 ### Google Play Services
 Procuramos implementar os serviços da Google para a versão Android com a finalidade de obter o primeiro e último nome do utilizador para utilizar nos Top Scores do jogo. No entanto encontramos imensas dificuldades acerca da implementação em projetos LibGDX. Após algumas horas a googlar pelos sítios mais negros da internet pedimos ajuda a um colega do curso que nos conseguiu guiar. Quando finalmente conseguimos a integração descobrimos uma nova informação - é necessário proceder ao pagamento de uma taxa de 25 euros para ter uma conta de Developer da Google. Face a isto e à incerteza se algum de nós ia publicar alguma nova aplicação no futuro decidimos não efetuar o pagamento. Apesar disto, deixamos a implementação no nosso projeto caso no futuro mudemos de ideias. 
 
 ### Pathfinding
Tentamos fazer uso da biblioteca GDX-Ai que implementa uma versão melhorada do algoritmo A* o que resultou em bastantes horas a ler a documentação das interfaces e classes da biblioteca uma vez que a Wiki está bastante incompleta e já não é atualizada há alguns anos. 

Apesar da nossa persistência não fomos capazes de obter sucesso e procuramos outra alternativa - usar *Raycasts* do Box2D para tentar construir um algoritmo menos eficiente mas que desse resultados minimamente bons. No entanto, encontramos um novo obstáculo. O método que esta biblioteca oferece é assíncrono o que tornava bastante complicada a implementação de um algoritmo. Apesar de termos conseguido ultrapassar esta dificuldade, surgiu outra... Quando dois corpos estão muito próximos o resultado que a biblioteca apresentava não tinha em conta este corpo muito próximo e portanto tornava fútil o processo todo.

Desistimos e a solução que encontramos foi de colocar os zombies sempre a andarem na direção do jogador. Esta solução não é de todo ótima mas no nosso mapa de jogo apresenta resultados bastante ok uma vez que não existem muitas paredes aonde os zombies possam colidir.

### Top Scores
Foi necessário armazenar os scores mais altos obtidos pelos jogadores num servidor externo. Para tal foi necessário aprender um pouco de PHP e combinar com o conhecimento que obtivemos noutra unidade curricular - Base de Dados.

[Aqui segue o programa que desenvolvemos.](https://pastebin.com/D0ydAXk8)

## Padrões de Desenho utilizados.
Ao longo do projeto fizemos uso dos seguintes padrões de desenho:
* **Arquitetura MVC** (*Model View Controller*) – usado para separar a lógica das estruturas de dados internas e definir uma interface que liga ambas. Torna a implementação de novas features menos caótica. Padrão de desenho associado às classes GameView, GameController e GameModel.
* **Singleton** - usado para facilitar a separação de módulos. A desvantagem evidente é que torna mais difícil inferir as dependências entre classes. Implementam este padrão as mesmas classes que implementam a arquitetura MVC.
* **Observer** – utilizado para a comunicação assíncrona e unidirecional do Box2D para a classe GameController acerca de colisões que ocorreram no mundo.
* **Factory** / **Flyweight** – estes 2 padrões são usados em conjunto para criar uma ViewFactory. Uma view é um objeto que representa visualmente um tipo de entidade. Não é necessário haver várias views para várias entidades do mesmo tipo uma vez que é possível reutilizar sempre a mesma, diminuindo o uso de memória por parte do nosso jogo. A fábrica é importante para abstrair este conceito mencionado anteriormente
* **Game Loop** – implementado pela classe GameController uma vez que é esta responsável por lidar com o Box2D para as simulações de física e colisões. Ao separar o update do render, torna-se possível que um dispositivo com baixo poder de processamento que não atinja um número de frames ideias por segundo, forneça ao jogador uma boa experiência uma vez que o movimento das entidades e cálculos efetuados internamente têm em conta a variação de tempo desde o último update.
* **Component** – uma entidade no nosso jogo está separada nos 3 domínios da arquitetura MVC. Facilita a implementação de novas features e é a forma mais natural de representar as entidades nesta arquitetura. Torna ainda mais fácil efetuar manutenção do código e corrigir bugs uma vez que é mais fácil isolá-los.

## Tempo investido.

Não podemos dar uma estimativa muito precisa do tempo que gastamos desde o planeamento e familiarização com o LibGDX/Box2D até à finalização do projeto mas sentimos que deve rondar 100 horas no total.

## O que aprendemos.
Trabalhar com uma nova framework bastante complexa apenas alguns meses após ter começado a aprender Java foi um enorme desafio. Sentimos que crescemos como desenvolvedores de software e podemos afirmar que no geral o processo foi bastante gratificante.

## Distribuição de Trabalho.

Sentimos que o trabalho foi realizado de uma forma justa entre ambos os membros da equipa. Não houve uma divisão linear da maioria das tarefas tendo acontecido que o desenvolvimento foi na maior parte incremental. 

No entanto podemos apontar alguns casos concretos de responsabilidades:

 * Ricardo Moura
	 - Integração Google Play Services
	 - Input Android
	 - Ajuste das animações  e do tamanho das sprites do jogador e zombie
	 - Sons das entidades
	 - HUD

* Fábio Oliveira
	- Implementação base dos padrões de desenho no projeto.
	- Unit Tests
	- Overlays
	- Top Scores
	- Mecanismo de Knockback

## Manual de Instruções.

Ao iniciar o jogo é pedido ao utilizador para clicar algures no ecrã. De seguida são apresentadas as 3 possíveis dificuldades que o jogo oferece: EASY, MEDIUM e INSANE. O aumento da dificuldade traduz-se num maior número de zombies por ronda.

![Click Anywhere Overlay](https://i.imgur.com/tHetwqv.png)

![Choose Difficulty Overlay](https://i.imgur.com/a5VUBO3.png)

O jogo é composto por dois distintos elementos: o atirador, controlado pelo jogador, e os zombies.

O objetivo é sobreviver o máximo número de rondas. Para tal efeito, o atirador tem 3 opções de armamento: metralhadora, pistola e faca. 

Os zombies têm como objetivo eliminar o atirador e quando lhe tocam fazem-no perder vida. Existem vários tipos de zombies: pequenos, médios e grandes. Quanto maior em tamanho um zombie é, menor velocidade tem mas mais resistência a tiros apresenta.

O jogador controla o atirador na versão desktop utilizando  as teclas WASD/setas para se mover, o rato para disparar, as teclas “1”, “2” e “3” para selecionar a arma e “r” para a recarregar. Na versão Android o movimento do atirador é realizado a partir de joy sticks virtuais existentes no ecrã. Para as restantes ações utilizam-se as rotações do telemóvel (giroscópio), na horizontal para disparar/recarregar a arma, e na vertical para mudar para outra arma.

O jogo funciona com rondas de ataques de zombies que se repetem, aumentando o nível de dificuldade sempre que o atirador elimina com sucesso todos os zombies.

A personagem controlada pelo utilizador começa com um nível de vida que vai descendo à medida que os zombies o atacam. No entanto a vida regenera-se automaticamente quando o atirador não leva dano de zombies durante uns segundos.

![First Round!](https://i.imgur.com/4t8udDE.png)

O jogo acaba quando o jogador atinge uma ronda cujo número não possa ser armazenado num inteiro signed de 64 bits e nesse caso o jogo explode, ou quando não consegue sobreviver às hordas de zombie que o perseguem.

Quando o jogo termina é atribuído ao jogador um score pelo desempenho e de seguida é apresentado a lista de jogadores que obtiveram as melhores classificações.

![Game Over Overlay](https://i.imgur.com/OhLWb6Q.png)

## Créditos.
Este projeto não tinha sido possível sem a implementação publicada de vários padrões de desenho por parte do professor André Restivo.

Gostavamos ainda de agradecer aos autores dos seguintes *assets* que utilizamos no nosso jogo:

* [Sons de Zombie](https://www.youtube.com/watch?v=wg8u3AQj1Ac)
* [Sprites do Jogador](https://opengameart.org/content/animated-top-down-survivor-player)
* [Sprites de Zombie](https://opengameart.org/content/animated-top-down-zombie)
