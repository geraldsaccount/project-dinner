import UserAvatar from "@/components/shared/user-avatar";
import { Alert } from "@/components/ui/alert";
import { UserDto } from "@/types";

type Props = {
  host: UserDto;
  dateTime: string;
};

const HostCallout = ({ host, dateTime }: Props) => {
  return (
    <Alert className="mb-2 flex items-center gap-3">
      <UserAvatar user={host} className="h-14 w-14" />
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
