import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { DinnerSummaryDto } from "@/types";
import { NavLink } from "react-router-dom";


type Props = {
  summary: DinnerSummaryDto;
};

const DinnerSummaryCard = ({ summary }: Props) => {
  const {
    uuid,
    dateTime,
    host,
    storyTitle,
    yourAssignedCharacterName,
  } = summary;


  return (
    <NavLink to={uuid} className="h-auto">
      <Card className="py-0 overflow-clip h-full">
        <div className="flex h-full">
          <div className="w-1/3 flex-shrink-0 flex items-center justify-center bg-gray-100">
            <img
              src="https://placehold.co/100x100"
              alt={storyTitle}
              className="object-cover w-full h-full"
            />
          </div>
          <div className="w-2/3 p-4">
            <CardHeader className="p-0 pb-2">
              <div className="font-bold text-lg">{storyTitle}</div>
              <div className="text-sm text-muted-foreground">
                Host: {host.name}
              </div>
            </CardHeader>
            <CardContent className="p-0">
              <div>
                <span className="font-medium">Date:</span>{" "}
                {new Date(dateTime).toLocaleString()}
              </div>
              <div>
                <span className="font-medium">Your Character:</span>{" "}
                {yourAssignedCharacterName ?? (
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

export default DinnerSummaryCard;
