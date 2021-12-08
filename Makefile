onos_url := http://localhost:8181/onos
onos_curl := curl --fail -sSL --user onos:rocks --noproxy localhost
onos_root := /home/sume/projects/onos
# app_name := org.onosproject.p4virtex
app_name := org.onosproject.virtual
#oar_root := ${onos_root}/bazel-bin/apps/p4virtex/onos-apps-p4virtex-oar.oar
oar_root := ${onos_root}/bazel-bin/apps/virtual/onos-apps-virtual-oar.oar
netcfg_file := ${onos_root}/apps/p4virtex/netcfg.json
netcfg1x1_file := ${onos_root}/apps/p4virtex/netcfg1x1.json
http_proxy := http://192.168.12.6:7890
srv6_p4_dir := /home/sume/projects/onos/apps/p4virtex/pipelines/srv6/src/main/resources
srv6_app_name := org.onosproject.pipelines.srv6
srv6_oar_root := ${onos_root}/bazel-bin/pipelines/srv6/onos-pipelines-srv6-oar.oar

compile_srv6_p4:
	docker run --rm -it -v /home/sume/projects/onos/pipelines/srv6/src/main/resources:/test p4lang/p4c
	cd /test & p4c --target bmv2 --arch v1model --p4runtime-files p4c-out/bmv2/srv6_p4info.txt --output p4c-out/bmv2 srv6.p4

srv6-app-install:
	$(info *** Installing and activate app in ONOS ... ***)
	${onos_curl} -X POST -HContent-Type:application/octet-stream '${onos_url}/v1/applications?activate=true' --data-binary @${srv6_oar_root}
	@echo

srv6-app-uninstall:
	$(info *** Uninstalling app from ONOS ... ***)
	${onos_curl} -X DELETE ${onos_url}/v1/applications/${srv6_app_name}
	@echo

app-build:
	cd $(onos_root)
	bazel build //apps/p4virtex/...:all

app-install:
	$(info *** Installing and activate app in ONOS ... ***)
	${onos_curl} -X POST -HContent-Type:application/octet-stream '${onos_url}/v1/applications?activate=true' --data-binary @${oar_root}
	@echo

app-uninstall:
	$(info *** Uninstalling app from ONOS ... ***)
	${onos_curl} -X DELETE ${onos_url}/v1/applications/${app_name}
	@echo

app-reinstall: app-uninstall app-install

app-deploy: app-build app-reinstall

onos_start:
	cd $(onos_root) && bazel run onos-local --action_env=HTTP_PROXY=$(http_proxy)

netcfg:
	${onos_curl} -X POST -H 'Content-Type:application/json' ${onos_url}/v1/network/configuration -d@${netcfg_file}

netcfg1x1:
	${onos_curl} -X POST -H 'Content-Type:application/json' ${onos_url}/v1/network/configuration -d@${netcfg1x1_file}