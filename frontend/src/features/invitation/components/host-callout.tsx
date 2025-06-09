import { Alert } from "@/components/ui/alert";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { UserDto } from "@/types";

type Props = {
  host: UserDto;
  dateTime: string;
};

const HostCallout = ({ host, dateTime }: Props) => {
  return (
    <Alert className="mb-2 flex items-center gap-3">
      <Avatar className="border-2 h-14 w-14 shrink-0">
        <AvatarImage src={host.avatarUrl} />
        <AvatarFallback>
          {host.name
            .split(" ")
            .map((n: string) => n[0])
            .join("")}
        </AvatarFallback>
      </Avatar>
      <span className="font-normal flex-1 flex flex-wrap items-center gap-x-1">
        <span className="font-bold">{host.name}</span>
        has invited you to play a mystery dinner on
        <span className="font-bold">
          {new Date(dateTime).toLocaleDateString(undefined, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
          .
        </span>
      </span>
    </Alert>
  );
};

export default HostCallout;
