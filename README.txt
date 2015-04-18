Eclipse Luna (me egkatestimeno to Maven plugin)

Android Studio

Apache Tomcat 8

Java 1.7.0_55


To DSBackendServer einai to workspace pou tha anoiksete me to Eclipse.
To DSAndroidApp einai to project pou tha anoiksete me to Android Studio.

1. Otan egkatastisete ton tomcat kante copy-paste ton fakelo "ssl" sto path "C:\Program Files\Apache Software Foundation\Tomcat 8.0".
Aytos o fakelos periexei ena self-signed certificate pou xreiazete o tomcat gia na akouei stin porta 8443 me SSL.


2. Gia na syndetheite me PGAdmin stin RDS vasi xrisimopoieiste ta credentials pou dinontai sto general_credentials.txt

3. Gia na syndetheite me Fielzilla sto EC2 instance pou exoume kante ta eksis:
	a) Anoikste ta Settings kai pigente sto Connection -> SFTP kai patiste Add keyfile. Dwste to "ec2_ds_security_key_filezilla.ppk"
	b) Ftiakste ena kainourgio Site me host: 52.16.137.213 , port: 22 , protocol: SFTP , Logon Type: Normal, User: ubuntu, Password: NO PASSWORD (keno)

4. Gia na syndetheite me putty sto EC2 instance pou exoume kante ta eksis:
	a) anoikste to putty kai gia host name valte : ubuntu@52.16.137.213, port: 22, connection type: SSH
	b) pigainte sto Connection -> SSH -> Auth kai ekei pou leei authentication parameters -> Private key file for authentication
		patiste Browse kai valte to "ec2_ds_security_key_putty.ppk"
	c) Ean thelete gia na min to kanete ayto synexeia swste to session kai kathe fore pou anoigete to putty dialekste to kai kante load.


PARATIRISEIS:

Genika min kanete tipota sto AWS pou tha xrewsoun ton logarismo mas (opws to na sikwsete ena EC2 instance) 
gt tha xrewthoume tsampa kai exw valei tin pistwtiki m mesa :P

Epeidi gia ta EC2 instances exoume 750 wres to mina (ayto simainei oti ean sikwsoume ena mixanima kai to exoume synexeia anoixto tha tis xrhsimopoihsei oles tis wres)
tha sikwnoume to instance otan theloume na kanoume douleia kai stin synexeia tha to kleinoume (Stop oxi Delete) gia na min xalame tsampa wres.
Etsi tha mas meinoun wres wste na sikwsoume kai alla mixanimata gia na testaroume to scalability. :)