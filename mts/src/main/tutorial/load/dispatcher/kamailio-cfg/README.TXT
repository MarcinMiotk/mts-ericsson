####
#### dans une ubuntu / debian
####

http://www.kamailio.org/w/2010/06/debian-and-ubuntu-apt-repositories/

apt-get install kamailio kamailio-mysql-modules mysql-server


####
#### dans kamailio.cfg, ajouter / modifier (�ventuellement �craser par celui pr�sent dans le dossier : attention ne marche pas toujours) :
####

#!define WITH_AUTH
#!define WITH_MYSQL

...

/* add local domain aliases */
alias="sip.france.fr"

...


####
#### �diter / remplacer kamctl (cf celui pr�sent dans le dossier)
####



####
#### initialiser la base
####
kamdbctl create

####
#### ajouter les users (cf script.sh)
####
kamctl add username password