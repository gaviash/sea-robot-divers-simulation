# Rapport de simulations polarisantes

## Objectif

Ce rapport sert a montrer comment les parametres principaux de la simulation changent l'equilibre entre les deux equipes :

- les robots, limites par la batterie mais capables d'aller tres loin ;
- les plongeurs, limites par l'oxygene mais capables de ramasser les tresors et l'oxygene.

La mesure utilisee est le score final par equipe. La colonne `Diff` vaut :

```text
Diff = Score Robots - Score Plongeurs
```

Donc :

- `Diff > 0` : avantage robots ;
- `Diff < 0` : avantage plongeurs ;
- `Diff proche de 0` : simulation equilibree.

Les simulations sont aleatoires, donc les valeurs exactes peuvent varier d'un lancement a l'autre. L'objectif ici n'est pas d'obtenir une preuve mathematique, mais de montrer des tendances nettes et exploitables pour la soutenance.

## Parametres de reference actuels

Les constantes actuelles du projet sont :

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

Ces parametres donnent une simulation assez lisible : les plongeurs restent competitifs, mais ils ne dominent plus automatiquement toutes les grandes simulations.

## Simulations classiques de reference

Fichiers :

- `exp_logs/polarisation_variants/reference_A30_T50_D10.log`
- `exp_logs/polarisation_variants/reference_A30_T50_D20.log`
- `exp_logs/polarisation_variants/reference_A40_T80_D20.log`
- `exp_logs/polarisation/reference_current_A30_T100_D10.log`

| Scenario | Robots | Plongeurs | Diff | Lecture |
|---|---:|---:|---:|---|
| 30 agents, 50 tours, 10x10 | 435 | 561 | -126 | Avantage plongeurs |
| 30 agents, 50 tours, 20x20 | 372 | 393 | -21 | Quasi equilibre |
| 40 agents, 80 tours, 20x20 | 849 | 769 | +80 | Leger avantage robots |
| 30 agents, 100 tours, 10x10 | 808 | 1140 | -332 | Avantage plongeurs amplifie par la duree |

Interpretation :

- Sur un petit terrain 10x10, les plongeurs trouvent plus facilement des ressources utiles, donc leur faible deplacement est moins handicapant.
- Sur un terrain 20x20, le grand deplacement du robot devient plus important, ce qui rapproche les scores.
- Augmenter le nombre de tours amplifie les avantages deja presents : si une equipe est un peu meilleure dans une configuration, l'ecart augmente avec le temps.

## Simulations polarisantes

Pour isoler les effets, les variantes suivantes ont ete lancees dans des copies temporaires des fichiers Java. Les sources principales du projet n'ont pas ete modifiees.

### Variante robot fort

Parametres modifies :

| Classe | Parametre | Reference | Variante |
|---|---:|---:|---:|
| `Robot` | `MAX_BATTERIE` | 40 | 80 |
| `Robot` | `BATTERIE_DEPART` | 40 | 80 |
| `Robot` | `USE_PER_TURN` | 5 | 3 |
| `Robot` | `RECHARGE_PER_TURN` | 5 | 10 |

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 628 | 464 | +164 |
| 30 agents, 50 tours, 20x20 | 617 | 376 | +241 |
| 40 agents, 80 tours, 20x20 | 1254 | 833 | +421 |

Conclusion : la batterie est un parametre tres sensible. Quand le robot a plus d'autonomie et se recharge plus vite, il transforme son grand deplacement en avantage massif, surtout sur les grands terrains.

### Variante plongeur fort

Parametres modifies :

| Classe | Parametre | Reference | Variante |
|---|---:|---:|---:|
| `Plongeur` | `OXYGENE_DEPART` | 40 | 70 |
| `Plongeur` | `USE_PER_TURN` | 6 | 3 |
| `Simulation` | quantite des bouteilles d'oxygene | 15 | 25 |
| `Oxygene` | perte naturelle par evolution | 3 | 1 |

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 185 | 381 | -196 |
| 30 agents, 50 tours, 20x20 | 223 | 269 | -46 |
| 40 agents, 80 tours, 20x20 | 420 | 556 | -136 |

Conclusion : rendre l'oxygene plus abondant et moins fragile favorise les plongeurs. Ils peuvent rester plus longtemps en comportement normal ou cupide, donc ils passent moins de tours a chercher de l'oxygene par urgence.

### Variante oxygene rare

Parametres modifies :

| Classe | Parametre | Reference | Variante |
|---|---:|---:|---:|
| `Plongeur` | `OXYGENE_DEPART` | 40 | 30 |
| `Plongeur` | `USE_PER_TURN` | 6 | 8 |
| `Simulation` | quantite des bouteilles d'oxygene | 15 | 10 |
| `Oxygene` | perte naturelle par evolution | 3 | 5 |

| Scenario | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 30 agents, 50 tours, 10x10 | 431 | 201 | +230 |
| 30 agents, 50 tours, 20x20 | 375 | 69 | +306 |
| 40 agents, 80 tours, 20x20 | 836 | 161 | +675 |

Conclusion : l'oxygene est le parametre le plus polarisant pour les plongeurs. Quand il devient rare, les plongeurs passent trop de temps en comportement de survie et marquent beaucoup moins. Les robots deviennent largement gagnants meme sans changer leurs propres constantes.

## Effet des grands parametres

### Taille du terrain

Comparaison de reference avec 30 agents et 50 tours :

| Terrain | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 10x10 | 435 | 561 | -126 |
| 20x20 | 372 | 393 | -21 |

Quand le terrain grandit, les ressources sont plus dispersees. Cela penalise davantage les plongeurs, car leur deplacement vaut 5, alors que celui des robots vaut 60. Le terrain plus grand rapproche donc les equipes, voire peut avantager les robots.

### Nombre de tours

Comparaison en 30 agents, terrain 10x10 :

| Tours | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| 50 | 424 | 569 | -145 |
| 100 | 808 | 1140 | -332 |

Plus la simulation dure longtemps, plus l'ecart cumule devient visible. Le nombre de tours n'inverse pas forcement la tendance, mais il l'amplifie.

### Batterie du robot

La variante robot fort montre que la batterie controle directement la regularite des robots. Avec une batterie plus grande, une consommation plus faible et une recharge rapide, les robots perdent moins de tours en recharge et ramassent plus de tresors.

Impact net sur 30 agents, 50 tours, 20x20 :

| Configuration | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| Reference | 372 | 393 | -21 |
| Robot fort | 617 | 376 | +241 |

### Oxygene du plongeur

L'oxygene agit sur plusieurs niveaux :

- l'oxygene de depart decide si le plongeur commence sereinement ou sous pression ;
- la consommation par tour decide la vitesse a laquelle il tombe en urgence ;
- la quantite des bouteilles decide si une recharge vaut vraiment le detour ;
- la degradation naturelle decide si une bouteille reste utile longtemps sur le terrain.

Impact net sur 30 agents, 50 tours, 20x20 :

| Configuration | Robots | Plongeurs | Diff |
|---|---:|---:|---:|
| Plongeur fort | 223 | 269 | -46 |
| Reference | 372 | 393 | -21 |
| Oxygene rare | 375 | 69 | +306 |

La rarete de l'oxygene fait chuter tres fortement le score des plongeurs. C'est donc le meilleur levier si on veut montrer une difference claire de comportement.

## Conclusion generale

La balance de la simulation depend surtout de trois familles de parametres :

1. La taille du terrain : plus le terrain est grand, plus les robots profitent de leur grand deplacement.
2. La batterie : plus elle est genereuse, moins les robots perdent de tours en recharge.
3. L'oxygene : c'est le levier le plus fort sur les plongeurs, car il influence leur comportement et leur capacite a continuer a chercher des tresors.

Le calibrage actuel est interessant pour une demonstration, car il permet d'obtenir des resultats differents selon la configuration sans qu'une equipe gagne toujours automatiquement.
