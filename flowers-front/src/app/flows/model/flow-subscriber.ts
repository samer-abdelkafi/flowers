import {Destination} from "./destination";

export interface FlowSubscriber {
    destination: Destination;
    condition: string;
}
