# Simulation Robots vs Plongeurs

![Java](https://img.shields.io/badge/Java-POO-blue)
![Status](https://img.shields.io/badge/status-projet%20universitaire-475569)

Projet Java de simulation d'exploration sur un terrain quadrille. Deux types d'agents se partagent le meme environnement :

- les **robots**, rapides et limites par leur batterie ;
- les **plongeurs**, plus lents et limites par leur oxygene.

L'objectif du projet est d'observer comment les parametres de simulation modifient l'equilibre entre les deux equipes.

Important : Les rapports ont étés générés par IA,selon les logs générés par le programme.
De meme,les fonctions de logs (les fonctions d'affichages),ont étées faite par IA,pour une grande economie de temps.

## Apercu

Chaque simulation cree un terrain, place des ressources aleatoirement, puis fait agir les agents tour par tour.

Les ressources principales sont :

| Ressource | Effet |
|---|---|
| `Tresor` | augmente le score de l'agent qui le ramasse |
| `Oxygene` | recharge les plongeurs et evolue avec le temps |

Les agents ont des comportements differents :

| Agent | Contraintes | Strategie generale |
|---|---|---|
| `Robot` | batterie, recharge | cherche les tresors avec un grand deplacement |
| `Plongeur` | oxygene, seuil bas/haut | alterne entre recherche d'oxygene et recherche de tresors |

## Architecture

```text
.
|-- Agent.java                  # Classe abstraite de base : position, distance, deplacement
|-- Explorateur.java            # Agent avec score et ramassage de ressources
|-- Robot.java                  # Explorateur rechargeable par batterie
|-- Plongeur.java               # Explorateur rechargeable en oxygene
|-- Rechargeable                # Interface pour les explorateurs rechargeables
|-- Ressource.java              # Ressource simple posee sur le terrain
|-- RessourceEvolutive.java     # Ressource pouvant evoluer avec le temps
|-- Oxygene.java                # Ressource evolutive pour les plongeurs
|-- Tresor.java                 # Ressource de score
|-- Terrain.java                # Grille de simulation
|-- Simulation.java             # Creation, tours, logs et score global
|-- SimulationException.java    # Exception dediee aux erreurs de simulation
|-- TestSimulation.java         # Lanceur parametrable
```

## Lancer le projet

Compiler :

```bash
javac *.java
```

Lancer une simulation avec les valeurs par defaut :

```bash
java TestSimulation
```

Lancer une simulation parametree :

```bash
java TestSimulation [nbAgents] [nbTours] [dimension]
```

Exemple :

```bash
java TestSimulation 30 50 20
```


## Parametres de reference

Les simulations de reference utilisent les constantes suivantes :

| Classe | Parametre | Valeur |
|---|---:|---:|
| `Plongeur` | `OXYGENE_DEPART` | 40 |
| `Plongeur` | `USE_PER_TURN` | 6 |
| `Plongeur` | `DEPLACEMENT` | 5 |
| `Oxygene` | quantite creee dans `Simulation` | 15 |
| `Oxygene` | perte naturelle par evolution | 3 |
| `Robot` | `BATTERIE_DEPART` | 40 |
| `Robot` | `MAX_BATTERIE` | 40 |
| `Robot` | `USE_PER_TURN` | 5 |
| `Robot` | `RECHARGE_PER_TURN` | 5 |
| `Robot` | `DEPLACEMENT` | 60 |

## Resultats de reference

La colonne `Diff` correspond a :

```text
Diff = Score Robots - Score Plongeurs
```

| Scenario | Robots | Plongeurs | Diff | Lecture |
|---|---:|---:|---:|---|
| 30 agents, 50 tours, 10x10 | 435 | 561 | -126 | avantage plongeurs |
| 30 agents, 50 tours, 20x20 | 372 | 393 | -21 | quasi equilibre |
| 40 agents, 80 tours, 20x20 | 849 | 769 | +80 | leger avantage robots |
| 30 agents, 100 tours, 10x10 | 808 | 1140 | -332 | avantage plongeurs amplifie |

Les resultats varient legerement d'un lancement a l'autre, car le placement des agents et des ressources est aleatoire.

## Simulations polarisantes

Pour comprendre les parametres les plus influents, plusieurs variantes ont ete lancees. Elles ne modifient pas les sources finales du projet : elles servent uniquement a comparer les effets.

### Robot favorise

Dans cette variante, le robot a plus de batterie, consomme moins et se recharge plus vite.

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 628 | 464 | +164 |
| 30 agents, 50 tours, 20x20 | 617 | 376 | +241 |
| 40 agents, 80 tours, 20x20 | 1254 | 833 | +421 |

Conclusion : la batterie est un levier tres fort. Plus elle est genereuse, moins les robots perdent de tours en recharge.

### Plongeur favorise

Dans cette variante, les plongeurs commencent avec plus d'oxygene, consomment moins et trouvent des bouteilles plus rentables.

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 185 | 381 | -196 |
| 30 agents, 50 tours, 20x20 | 223 | 269 | -46 |
| 40 agents, 80 tours, 20x20 | 420 | 556 | -136 |

Conclusion : un plongeur qui gere moins souvent l'urgence d'oxygene peut rester plus longtemps oriente vers les tresors.

### Oxygene rare

Dans cette variante, les plongeurs commencent avec moins d'oxygene, consomment plus, et les bouteilles deviennent moins utiles.

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 431 | 201 | +230 |
| 30 agents, 50 tours, 20x20 | 375 | 69 | +306 |
| 40 agents, 80 tours, 20x20 | 836 | 161 | +675 |

Conclusion : l'oxygene est le parametre le plus polarisant pour les plongeurs. Quand il devient rare, ils passent trop de tours en comportement de survie.

## Ce que montrent les simulations

| Parametre | Effet principal |
|---|---|
| Taille du terrain | favorise les robots quand les ressources sont dispersees |
| Nombre de tours | amplifie les ecarts deja presents |
| Batterie du robot | controle la regularite des robots |
| Oxygene du plongeur | controle fortement le score des plongeurs |

Le calibrage actuel est interessant parce qu'il ne donne pas toujours le meme gagnant : le resultat depend vraiment du terrain, du nombre de tours et des contraintes de ressources.

## Logs et rapports

Les logs conserves sont limites aux essais utiles :

- [Logs de reference](logs/polarisation/)
- [Logs polarisants](logs/polarisation_variants/)
- [Synthese CSV](logs/polarisation_variants/summary_polarisation.csv)

## Notes techniques

- Le projet utilise uniquement Java standard.
- `SimulationException` centralise les erreurs propres a la simulation.
- Les logs peuvent etre volumineux car la simulation affiche chaque tour et chaque action.
