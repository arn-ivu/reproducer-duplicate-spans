# reproducer-duplicate-spans

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

## The issue
The opentelemetry exporter sometimes restarts the connection to the collector, and when it does
it often resends spans that were already sent. This will then result in duplicate spans in the collector.

## How to reproduce
Start this program in dev mode. 
Open grafana > explore. You can use the following traceQL to identify affected traces:
```TraceQL
{resource.service.name="reproducer-duplicate-spans"} | count() > 2
```
Since this is a timing issue, you might need to wait for quite some time (~10 minutes) before the issue occurs.
It might be necessary to tweak the delays in Main.java to reproduce the issue on your machine.


## monitoring the traffic
The tcp traffic between the application and the collector can be monitored using wireshark.
Use filters like `tcp.port == 4317` (grpc) or `tcp.port == 4318` (http/protobuf) to only show 
the traffic between the application and the collector.


## Example
Some data is already assembled in the `example` folder.
- one trace with 2 spans as expected for reference
- one trace with duplicated spans to illustrate the problem
- the wireshark capture of the traffic between the application and the collector
  - grpc traffic from 12:33-12:52
    - duplicate spans occurring at 12:38:57 and 12:40:09
  - http/protobuf traffic from 12:53-12:58
    - duplicate spans occurring at 12:57:03 and 12:57:24
  - exported in pcapng format (wireshark can open this) and pcap format (RedHat 6.1 tcpdump)



