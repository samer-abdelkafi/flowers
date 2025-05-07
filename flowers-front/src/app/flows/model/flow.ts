import {Destination} from "./destination";
import {FlowSubscriber} from './flow-subscriber';


export interface Flow {
    id: number;
    name: string;
    publishers: Destination[];
    subscribers: FlowSubscriber[];
    enabled: boolean;
    status: "started" | "stopped" | "error";
}
