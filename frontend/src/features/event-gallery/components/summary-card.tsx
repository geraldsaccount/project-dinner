import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { NavLink } from "react-router-dom";

export type SessionSummary = {
  sessionId: string;
  hostName: string;
  storyName: string;
  assignedCharacterName: string | null;
  sessionDate: string;
  isHost: boolean;
};

type SessionSummaryCardProps = {
  summary: SessionSummary;
};

const SessionSummaryCard = ({ summary }: SessionSummaryCardProps) => {
  const {
    sessionId,
    hostName,
    storyName,
    assignedCharacterName,
    sessionDate,
    isHost,
  } = summary;

  return (
    <NavLink to={sessionId} className="h-auto">
      <Card className="py-0 overflow-clip h-full">
        <div className="flex h-full">
          <div className="w-1/3 flex-shrink-0 flex items-center justify-center bg-gray-100">
            <img
              src="https://placehold.co/100x100"
              alt={storyName}
              className="object-cover w-full h-full"
            />
          </div>
          <div className="w-2/3 p-4">
            <CardHeader className="p-0 pb-2">
              <div className="font-bold text-lg">{storyName}</div>
              <div className="text-sm text-muted-foreground">
                Host: {isHost ? "You" : hostName}
              </div>
            </CardHeader>
            <CardContent className="p-0">
              <div>
                <span className="font-medium">Date:</span>{" "}
                {new Date(sessionDate).toLocaleString()}
              </div>
              <div>
                <span className="font-medium">Your Character:</span>{" "}
                {assignedCharacterName ?? (
                  <span className="italic text-muted-foreground">
                    Not assigned
                  </span>
                )}
              </div>
            </CardContent>
          </div>
        </div>
      </Card>
    </NavLink>
  );
};

export default SessionSummaryCard;
