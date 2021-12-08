COMPILE_DEPS = CORE_DEPS + JACKSON + REST + CLI + [
    "@metrics_core//jar",
    "//core/common:onos-core-common",
    "//core/net:onos-core-net",
    "//core/store/serializers:onos-core-serializers",
    "//apps/p4virtex/api:onos-apps-p4virtex-api",
]

TEST_DEPS = TEST_ADAPTERS

osgi_jar_with_tests(
    karaf_command_packages = ["org.onosproject.p4virtex.cli"],
    suppress_checkstyle = True,
    test_deps = TEST_DEPS,
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)
