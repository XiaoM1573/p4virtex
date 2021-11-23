COMPILE_DEPS = CORE_DEPS + [
    "@minimal_json//jar",
    "//protocols/p4runtime/model:onos-protocols-p4runtime-model",
    "//protocols/p4runtime/api:onos-protocols-p4runtime-api",
]

BUNDLES = [
    "//pipelines/srv6:onos-pipelines-srv6",
]

TEST_DEPS = TEST_ADAPTERS

osgi_jar_with_tests(
    suppress_checkstyle = True,
    test_deps = TEST_DEPS,
    visibility = ["//visibility:public"],
    deps = COMPILE_DEPS,
)

onos_app(
    app_name = "org.onosproject.pipelines.srv6",
    category = "Pipeline",
    description = "Provides pipelines with SRv6.",
    included_bundles = BUNDLES,
    required_apps = [
        "org.onosproject.drivers.p4runtime",
    ],
    title = "SRv6 Pipelines",
    url = "http://onosproject.org",
)
