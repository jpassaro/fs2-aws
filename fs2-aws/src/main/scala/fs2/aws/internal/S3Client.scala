package fs2.aws.internal

import java.io.InputStream

import cats.effect.Effect
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._

import scala.collection.JavaConverters._
import scala.util.control.Exception

private[aws] trait S3Client[F[_]] {
  private lazy val client = S3AsyncClient.create

  def getObjectContentOrError(getObjectRequest: GetObjectRequest)(
      implicit F: Effect[F]): F[Either[Throwable, InputStream]] =
    F.delay(Exception.nonFatalCatch either client.getObject(getObjectRequest).getObjectContent)

  def getObjectContent(getObjectRequest: GetObjectRequest)(implicit F: Effect[F]): F[InputStream] =
    F.delay(client.getObject(getObjectRequest).getObjectContent)

  def initiateMultipartUpload(request: CreateMultipartUploadRequest)(
      implicit F: Effect[F]): F[CreateMultipartUploadRequest] =
    F.delay(client.createMultipartUpload(request))

  def uploadPart(uploadPartRequest: UploadPartRequest)(implicit F: Effect[F]): F[UploadPartResponse] =
    F.delay(client.uploadPart(uploadPartRequest))

  def completeMultipartUpload(completeMultipartUploadRequest: CompleteMultipartUploadRequest)(
      implicit F: Effect[F]): F[CompleteMultipartUploadResponse] =
    F.delay(client.completeMultipartUpload(completeMultipartUploadRequest))

  def s3ObjectSummaries(listObjectsV2Request: ListObjectsV2Request)(
      implicit F: Effect[F]): F[List[S3ObjectSummary]] =
    F.delay(client.listObjectsV2(listObjectsV2Request).getObjectSummaries.asScala.toList)

  def getObject(objectRequest: GetObjectRequest)(implicit F: Effect[F]): F[S3Object] = {
    F.delay(client.getObject(objectRequest))
  }

}
